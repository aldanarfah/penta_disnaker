package com.disnaker.penta.service;

import com.disnaker.penta.dto.KomunitasFotoResponseDto;
import com.disnaker.penta.entity.KomunitasFoto;
import com.disnaker.penta.entity.KomunitasKegiatan;
import com.disnaker.penta.exception.BusinessRuleException;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.KomunitasFotoRepository;
import com.disnaker.penta.repository.KomunitasKegiatanRepository;
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
public class KomunitasFotoService {

    public static final int MAKS_FOTO_PER_KEGIATAN = 2;
    private static final String SUB_FOLDER = "komunitas-foto";

    private final KomunitasFotoRepository fotoRepository;
    private final KomunitasKegiatanRepository kegiatanRepository;
    private final FileStorageService fileStorageService;

    public List<KomunitasFotoResponseDto> findByKegiatanId(Long kegiatanId) {
        return fotoRepository.findByKegiatanIdOrderByIdAsc(kegiatanId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    /** Ambil foto banyak kegiatan sekaligus (1 query), supaya daftar kegiatan tidak lambat */
    public Map<Long, List<KomunitasFotoResponseDto>> findByKegiatanIds(Collection<Long> kegiatanIds) {
        if (kegiatanIds.isEmpty()) {
            return Map.of();
        }
        return fotoRepository.findByKegiatanIdInOrderByIdAsc(kegiatanIds)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.groupingBy(KomunitasFotoResponseDto::getKegiatanId));
    }

    public KomunitasFoto findEntityById(Long fotoId) {
        return fotoRepository.findById(fotoId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Foto kegiatan", fotoId));
    }

    public List<KomunitasFotoResponseDto> upload(Long kegiatanId, List<MultipartFile> files) {
        KomunitasKegiatan kegiatan = kegiatanRepository.findById(kegiatanId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Kegiatan komunitas", kegiatanId));

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
            List<KomunitasFotoResponseDto> hasil = new ArrayList<>();
            for (MultipartFile file : berkas) {
                String path = fileStorageService.store(file, SUB_FOLDER);
                sudahDisimpan.add(path);

                KomunitasFoto foto = KomunitasFoto.builder()
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
        KomunitasFoto foto = findEntityById(fotoId);
        fileStorageService.delete(foto.getPathFile());
        fotoRepository.delete(foto);
    }

    /** Dipakai saat satu kegiatan dihapus: hapus semua file foto fisik + barisnya */
    public void hapusSemuaByKegiatan(Long kegiatanId) {
        List<KomunitasFoto> daftar = fotoRepository.findByKegiatanIdOrderByIdAsc(kegiatanId);
        for (KomunitasFoto foto : daftar) {
            fileStorageService.delete(foto.getPathFile());
        }
        fotoRepository.deleteAll(daftar);
    }

    public KomunitasFotoResponseDto toResponseDto(KomunitasFoto entity) {
        return KomunitasFotoResponseDto.builder()
                .id(entity.getId())
                .kegiatanId(entity.getKegiatan().getId())
                .namaFile(entity.getNamaFile())
                .uploadedAt(entity.getUploadedAt())
                .url("/api/disabilitas/komunitas/foto/" + entity.getId())
                .build();
    }
}