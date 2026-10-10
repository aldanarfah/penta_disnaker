package com.disnaker.penta.service;

import com.disnaker.penta.dto.PemberdayaanFotoResponseDto;
import com.disnaker.penta.dto.PemberdayaanRequestDto;
import com.disnaker.penta.dto.PemberdayaanResponseDto;
import com.disnaker.penta.entity.PemberdayaanKegiatan;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.PemberdayaanKegiatanRepository;
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
public class PemberdayaanService {

    private static final String SUB_FOLDER_LAPORAN = "pemberdayaan-laporan";
    private static final String[] NAMA_HARI = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private static final DateTimeFormatter FORMAT_WAKTU = DateTimeFormatter.ofPattern("HH:mm");

    private final PemberdayaanKegiatanRepository kegiatanRepository;
    private final PemberdayaanFotoService fotoService;
    private final FileStorageService fileStorageService;

    /** tahun dan cari sama-sama opsional. cari mencocokkan nama kegiatan atau tempat. Terbaru di atas. */
    public List<PemberdayaanResponseDto> findAll(Integer tahun, String cari) {
        String kataKunci = (cari == null) ? "" : cari.trim();
        List<PemberdayaanKegiatan> daftar = (tahun == null)
                ? kegiatanRepository.cariSemua(kataKunci)
                : kegiatanRepository.cariPerTahun(tahun, kataKunci);

        List<Long> ids = daftar.stream().map(PemberdayaanKegiatan::getId).toList();
        Map<Long, List<PemberdayaanFotoResponseDto>> fotoPerKegiatan = fotoService.findByKegiatanIds(ids);

        return daftar.stream()
                .map(k -> toResponseDto(k, fotoPerKegiatan.getOrDefault(k.getId(), List.of())))
                .toList();
    }

    public PemberdayaanResponseDto findById(Long id) {
        PemberdayaanKegiatan entity = findEntityById(id);
        return toResponseDto(entity, fotoService.findByKegiatanId(id));
    }

    public PemberdayaanKegiatan findEntityById(Long id) {
        return kegiatanRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Kegiatan pemberdayaan", id));
    }

    public PemberdayaanResponseDto create(PemberdayaanRequestDto request) {
        PemberdayaanKegiatan entity = PemberdayaanKegiatan.builder()
                .namaKegiatan(request.getNamaKegiatan().trim())
                .tanggal(request.getTanggal())
                .waktu(request.getWaktu())
                .tempat(request.getTempat().trim())
                .jumlahPeserta(request.getJumlahPeserta())
                .build();
        PemberdayaanKegiatan saved = kegiatanRepository.save(entity);
        return toResponseDto(saved, List.of());
    }

    public PemberdayaanResponseDto update(Long id, PemberdayaanRequestDto request) {
        PemberdayaanKegiatan existing = findEntityById(id);

        existing.setNamaKegiatan(request.getNamaKegiatan().trim());
        existing.setTanggal(request.getTanggal());
        existing.setWaktu(request.getWaktu());
        existing.setTempat(request.getTempat().trim());
        existing.setJumlahPeserta(request.getJumlahPeserta());

        // saveAndFlush supaya updatedAt di response sudah nilai terbaru
        PemberdayaanKegiatan updated = kegiatanRepository.saveAndFlush(existing);
        return toResponseDto(updated, fotoService.findByKegiatanId(id));
    }

    public void delete(Long id) {
        PemberdayaanKegiatan existing = findEntityById(id);

        // Hapus file fisik (foto + laporan) supaya tidak jadi file sampah di server
        fotoService.hapusSemuaByKegiatan(id);
        if (existing.getLaporanPath() != null) {
            fileStorageService.delete(existing.getLaporanPath());
        }
        kegiatanRepository.delete(existing);
    }

    /** Upload laporan baru. Kalau sudah ada laporan, file lama otomatis diganti. */
    public PemberdayaanResponseDto uploadLaporan(Long id, MultipartFile file) {
        PemberdayaanKegiatan existing = findEntityById(id);
        UploadValidator.validasiLaporan(file);

        // Simpan file baru dulu, baru hapus yang lama, supaya laporan lama tidak hilang kalau penyimpanan gagal
        String pathBaru = fileStorageService.store(file, SUB_FOLDER_LAPORAN);
        String pathLama = existing.getLaporanPath();

        existing.setLaporanPath(pathBaru);
        existing.setLaporanNama(UploadValidator.namaAman(file));
        existing.setLaporanUkuran(file.getSize());
        PemberdayaanKegiatan updated = kegiatanRepository.saveAndFlush(existing);

        if (pathLama != null) {
            fileStorageService.delete(pathLama);
        }
        return toResponseDto(updated, fotoService.findByKegiatanId(id));
    }

    public PemberdayaanResponseDto hapusLaporan(Long id) {
        PemberdayaanKegiatan existing = findEntityById(id);

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

    private PemberdayaanResponseDto toResponseDto(PemberdayaanKegiatan entity, List<PemberdayaanFotoResponseDto> foto) {
        boolean adaLaporan = entity.getLaporanPath() != null;
        return PemberdayaanResponseDto.builder()
                .id(entity.getId())
                .namaKegiatan(entity.getNamaKegiatan())
                .tanggal(entity.getTanggal())
                .hari(NAMA_HARI[entity.getTanggal().getDayOfWeek().getValue() - 1])
                .waktu(entity.getWaktu().format(FORMAT_WAKTU))
                .tempat(entity.getTempat())
                .jumlahPeserta(entity.getJumlahPeserta())
                .foto(foto)
                .laporanNama(entity.getLaporanNama())
                .laporanUkuran(entity.getLaporanUkuran())
                .laporanUrl(adaLaporan
                        ? "/api/disabilitas/pemberdayaan/" + entity.getId() + "/laporan/download"
                        : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}