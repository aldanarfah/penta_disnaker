package com.disnaker.penta.service;

import com.disnaker.penta.dto.KomunitasFotoResponseDto;
import com.disnaker.penta.dto.KomunitasRequestDto;
import com.disnaker.penta.dto.KomunitasResponseDto;
import com.disnaker.penta.entity.KomunitasKegiatan;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.KomunitasKegiatanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class KomunitasService {

    private static final String SUB_FOLDER_LAPORAN = "komunitas-laporan";
    private static final String[] NAMA_HARI = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private static final DateTimeFormatter FORMAT_WAKTU = DateTimeFormatter.ofPattern("HH:mm");

    private final KomunitasKegiatanRepository kegiatanRepository;
    private final KomunitasFotoService fotoService;
    private final FileStorageService fileStorageService;

    /** tahun dan cari sama-sama opsional. cari mencocokkan nama kegiatan atau tempat. Terbaru di atas. */
    public List<KomunitasResponseDto> findAll(Integer tahun, String cari) {
        String kataKunci = (cari == null) ? "" : cari.trim();
        List<KomunitasKegiatan> daftar = (tahun == null)
                ? kegiatanRepository.cariSemua(kataKunci)
                : kegiatanRepository.cariPerTahun(tahun, kataKunci);

        List<Long> ids = daftar.stream().map(KomunitasKegiatan::getId).toList();
        Map<Long, List<KomunitasFotoResponseDto>> fotoPerKegiatan = fotoService.findByKegiatanIds(ids);

        return daftar.stream()
                .map(k -> toResponseDto(k, fotoPerKegiatan.getOrDefault(k.getId(), List.of())))
                .toList();
    }

    public KomunitasResponseDto findById(Long id) {
        KomunitasKegiatan entity = findEntityById(id);
        return toResponseDto(entity, fotoService.findByKegiatanId(id));
    }

    public KomunitasKegiatan findEntityById(Long id) {
        return kegiatanRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Kegiatan komunitas", id));
    }

    public KomunitasResponseDto create(KomunitasRequestDto request) {
        KomunitasKegiatan entity = KomunitasKegiatan.builder()
                .namaKegiatan(request.getNamaKegiatan().trim())
                .tanggal(request.getTanggal())
                .waktu(request.getWaktu())
                .tempat(request.getTempat().trim())
                .build();
        KomunitasKegiatan saved = kegiatanRepository.save(entity);
        return toResponseDto(saved, List.of());
    }

    public KomunitasResponseDto update(Long id, KomunitasRequestDto request) {
        KomunitasKegiatan existing = findEntityById(id);

        existing.setNamaKegiatan(request.getNamaKegiatan().trim());
        existing.setTanggal(request.getTanggal());
        existing.setWaktu(request.getWaktu());
        existing.setTempat(request.getTempat().trim());

        // saveAndFlush supaya updatedAt di response sudah nilai terbaru
        KomunitasKegiatan updated = kegiatanRepository.saveAndFlush(existing);
        return toResponseDto(updated, fotoService.findByKegiatanId(id));
    }

    public void delete(Long id) {
        KomunitasKegiatan existing = findEntityById(id);

        // Hapus file fisik (foto + laporan) supaya tidak jadi file sampah di server
        fotoService.hapusSemuaByKegiatan(id);
        if (existing.getLaporanPath() != null) {
            fileStorageService.delete(existing.getLaporanPath());
        }
        kegiatanRepository.delete(existing);
    }

    /** Upload laporan baru. Kalau sudah ada laporan, file lama otomatis diganti. */
    public KomunitasResponseDto uploadLaporan(Long id, MultipartFile file) {
        KomunitasKegiatan existing = findEntityById(id);
        UploadValidator.validasiLaporan(file);

        // Simpan file baru dulu, baru hapus yang lama, supaya laporan lama tidak hilang kalau penyimpanan gagal
        String pathBaru = fileStorageService.store(file, SUB_FOLDER_LAPORAN);
        String pathLama = existing.getLaporanPath();

        existing.setLaporanPath(pathBaru);
        existing.setLaporanNama(UploadValidator.namaAman(file));
        existing.setLaporanUkuran(file.getSize());
        KomunitasKegiatan updated = kegiatanRepository.saveAndFlush(existing);

        if (pathLama != null) {
            fileStorageService.delete(pathLama);
        }
        return toResponseDto(updated, fotoService.findByKegiatanId(id));
    }

    public KomunitasResponseDto hapusLaporan(Long id) {
        KomunitasKegiatan existing = findEntityById(id);

        if (existing.getLaporanPath() != null) {
            fileStorageService.delete(existing.getLaporanPath());
            existing.setLaporanPath(null);
            existing.setLaporanNama(null);
            existing.setLaporanUkuran(null);
            existing = kegiatanRepository.saveAndFlush(existing);
        }
        return toResponseDto(existing, fotoService.findByKegiatanId(id));
    }

    // ==================== Mapper Entity -> DTO ====================

    private KomunitasResponseDto toResponseDto(KomunitasKegiatan entity, List<KomunitasFotoResponseDto> foto) {
        boolean adaLaporan = entity.getLaporanPath() != null;
        return KomunitasResponseDto.builder()
                .id(entity.getId())
                .namaKegiatan(entity.getNamaKegiatan())
                .tanggal(entity.getTanggal())
                .hari(NAMA_HARI[entity.getTanggal().getDayOfWeek().getValue() - 1])
                .waktu(entity.getWaktu().format(FORMAT_WAKTU))
                .tempat(entity.getTempat())
                .foto(foto)
                .laporanNama(entity.getLaporanNama())
                .laporanUkuran(entity.getLaporanUkuran())
                .laporanUrl(adaLaporan
                        ? "/api/disabilitas/komunitas/" + entity.getId() + "/laporan/download"
                        : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}