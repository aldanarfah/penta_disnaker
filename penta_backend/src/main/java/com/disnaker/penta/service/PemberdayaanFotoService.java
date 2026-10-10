package com.disnaker.penta.service;

import com.disnaker.penta.dto.PemberdayaanFotoResponseDto;
import com.disnaker.penta.entity.PemberdayaanFoto;
import com.disnaker.penta.entity.PemberdayaanKegiatan;
import com.disnaker.penta.exception.BusinessRuleException;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.PemberdayaanFotoRepository;
import com.disnaker.penta.repository.PemberdayaanKegiatanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PemberdayaanFotoService {

    public static final int MAKS_FOTO_PER_KEGIATAN = 2;
    private static final String SUB_FOLDER = "pemberdayaan-foto";

    private final PemberdayaanFotoRepository fotoRepository;
    private final PemberdayaanKegiatanRepository kegiatanRepository;
    private final FileStorageService fileStorageService;

    public List<PemberdayaanFotoResponseDto> findByKegiatanId(Long kegiatanId) {
        return fotoRepository.findByKegiatanIdOrderByIdAsc(kegiatanId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    /** Ambil foto banyak kegiatan sekaligus (1 query), supaya daftar kegiatan tidak lambat */
    public Map<Long, List<PemberdayaanFotoResponseDto>> findByKegiatanIds(Collection<Long> kegiatanIds) {
        if (kegiatanIds.isEmpty()) {
            return Map.of();
        }
        return fotoRepository.findByKegiatanIdInOrderByIdAsc(kegiatanIds)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.groupingBy(PemberdayaanFotoResponseDto::getKegiatanId));
    }

    public PemberdayaanFoto findEntityById(Long fotoId) {
        return fotoRepository.findById(fotoId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Foto kegiatan pemberdayaan", fotoId));
    }

    public List<PemberdayaanFotoResponseDto> upload(Long kegiatanId, List<MultipartFile> files) {
        PemberdayaanKegiatan kegiatan = kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Kegiatan pemberdayaan", kegiatanId));

        List<MultipartFile> input = files != null ? files : List.of();
        List<MultipartFile> berkas = input.stream()
                .filter(f -> f != null && !f.isEmpty())
                .toList();
        if (berkas.isEmpty()) {
            throw new BusinessRuleException("Pilih minimal 1 foto untuk diunggah");
        }

        long sudahAda = fotoRepository.countByKegiatanId(kegiatanId);
        if (sudahAda + berkas.size() > MAKS_FOTO_PER_KEGIATAN) {
            throw new BusinessRuleException("Maksimal " + MAKS_FOTO_PER_KEGIATAN
                    + " foto per kegiatan (saat ini sudah ada " + sudahAda
                    + "). Hapus salah satu foto untuk menggantinya");
        }

        // Periksa SEMUA file dulu sebelum ada yang disimpan
        berkas.forEach(UploadValidator::validasiFoto);

        List<String> sudahDisimpan = new ArrayList<>();
        try {
            List<PemberdayaanFotoResponseDto> hasil = new ArrayList<>();
            for (MultipartFile file : berkas) {
                String path = fileStorageService.store(file, SUB_FOLDER);
                sudahDisimpan.add(path);

                PemberdayaanFoto foto = PemberdayaanFoto.builder()
                        .kegiatan(kegiatan)
                        .namaFile(UploadValidator.namaAman(file))
                        .pathFile(path)
                        .build();
                hasil.add(toResponseDto(fotoRepository.save(foto)));
            }
            return hasil;
        } catch (RuntimeException e) {
            // Database dibatalkan otomatis, file fisik yang sudah terlanjur disimpan dibersihkan di sini
            for (String path : sudahDisimpan) {
                try {
                    fileStorageService.delete(path);
                } catch (RuntimeException ignored) {
                    // abaikan, yang penting error aslinya tetap dilempar
                }
            }
            throw e;
        }
    }

    public void delete(Long fotoId) {
        PemberdayaanFoto foto = findEntityById(fotoId);
        fileStorageService.delete(foto.getPathFile());
        fotoRepository.delete(foto);
    }

    /** Dipakai saat satu kegiatan dihapus: hapus semua file foto fisik + barisnya */
    public void hapusSemuaByKegiatan(Long kegiatanId) {
        List<PemberdayaanFoto> daftar = fotoRepository.findByKegiatanIdOrderByIdAsc(kegiatanId);
        for (PemberdayaanFoto foto : daftar) {
            fileStorageService.delete(foto.getPathFile());
        }
        fotoRepository.deleteAll(daftar);
    }

    public PemberdayaanFotoResponseDto toResponseDto(PemberdayaanFoto entity) {
        return PemberdayaanFotoResponseDto.builder()
                .id(entity.getId())
                .kegiatanId(entity.getKegiatan().getId())
                .namaFile(entity.getNamaFile())
                .uploadedAt(entity.getUploadedAt())
                .url("/api/disabilitas/pemberdayaan/foto/" + entity.getId())
                .build();
    }
}