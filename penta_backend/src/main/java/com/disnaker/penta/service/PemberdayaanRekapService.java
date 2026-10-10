package com.disnaker.penta.service;

import com.disnaker.penta.dto.PemberdayaanFotoResponseDto;
import com.disnaker.penta.dto.PemberdayaanRekapKegiatanDto;
import com.disnaker.penta.dto.PemberdayaanRekapResponseDto;
import com.disnaker.penta.dto.PemberdayaanRekapTahunDto;
import com.disnaker.penta.entity.PemberdayaanKegiatan;
import com.disnaker.penta.entity.enums.StatusTahunPemberdayaan;
import com.disnaker.penta.exception.BusinessRuleException;
import com.disnaker.penta.repository.PemberdayaanKegiatanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PemberdayaanRekapService {

    /** "Tahun ini" dihitung berdasarkan jam server di zona waktu Lumajang */
    private static final ZoneId ZONA_SERVER = ZoneId.of("Asia/Jakarta");

    // Sama dengan yang dipakai PemberdayaanService (sengaja diulang supaya file yang sudah teruji tidak diubah)
    private static final String[] NAMA_HARI = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private static final DateTimeFormatter FORMAT_WAKTU = DateTimeFormatter.ofPattern("HH:mm");

    private final PemberdayaanKegiatanRepository kegiatanRepository;
    private final PemberdayaanFotoService fotoService;

    /** tahun kosong = tahun berjalan menurut jam server */
    public PemberdayaanRekapResponseDto rekap(Integer tahunParam) {
        int tahunIni = LocalDate.now(ZONA_SERVER).getYear();
        int tahun = (tahunParam != null) ? tahunParam : tahunIni;
        if (tahun < 2000 || tahun > 2100) {
            throw new BusinessRuleException("Tahun tidak valid (harus antara 2000 dan 2100)");
        }

        // Semua kegiatan semua tahun (kata kunci kosong = tanpa filter), sudah urut terbaru di atas.
        // Untuk skala data kegiatan per tahun sudah cukup; kalau nanti sangat besar bisa diganti query agregat.
        List<PemberdayaanKegiatan> semua = kegiatanRepository.cariSemua("");
        List<Long> ids = semua.stream().map(PemberdayaanKegiatan::getId).toList();
        Map<Long, List<PemberdayaanFotoResponseDto>> fotoPerKegiatan = fotoService.findByKegiatanIds(ids);
        Map<Integer, List<PemberdayaanKegiatan>> kegiatanPerTahun = semua.stream()
                .collect(Collectors.groupingBy(k -> k.getTanggal().getYear()));

        // Daftar tahun: dari tahun pertama yang punya data sampai tahun ini (kalau belum ada data: hanya tahun ini)
        int tahunAwal = kegiatanPerTahun.keySet().stream()
                .min(Integer::compareTo)
                .map(t -> Math.min(t, tahunIni))
                .orElse(tahunIni);

        List<PemberdayaanRekapTahunDto> rekapPerTahun = new ArrayList<>();
        int tahunTerlaksana = 0;
        for (int t = tahunIni; t >= tahunAwal; t--) {
            List<PemberdayaanKegiatan> kegiatanTahun = kegiatanPerTahun.getOrDefault(t, List.of());
            StatusTahunPemberdayaan status = tentukanStatus(t, tahunIni, !kegiatanTahun.isEmpty());
            if (status == StatusTahunPemberdayaan.TERLAKSANA) {
                tahunTerlaksana++;
            }
            rekapPerTahun.add(PemberdayaanRekapTahunDto.builder()
                    .tahun(t)
                    .jumlahKegiatan(kegiatanTahun.size())
                    .totalPeserta(hitungPeserta(kegiatanTahun))
                    .totalFoto(hitungFoto(kegiatanTahun, fotoPerKegiatan))
                    .laporanTerunggah(hitungLaporan(kegiatanTahun))
                    .status(status)
                    .build());
        }

        List<PemberdayaanKegiatan> kegiatanDipilih = kegiatanPerTahun.getOrDefault(tahun, List.of());

        return PemberdayaanRekapResponseDto.builder()
                .tahun(tahun)
                .totalKegiatan(kegiatanDipilih.size())
                .totalPeserta(hitungPeserta(kegiatanDipilih))
                .totalFoto(hitungFoto(kegiatanDipilih, fotoPerKegiatan))
                .laporanTerunggah(hitungLaporan(kegiatanDipilih))
                .tahunBerjalan(tahunIni - tahunAwal + 1)
                .tahunTerlaksana(tahunTerlaksana)
                .rekapPerTahun(rekapPerTahun)
                .kegiatan(kegiatanDipilih.stream()
                        .map(k -> toRekapKegiatan(k, fotoPerKegiatan.getOrDefault(k.getId(), List.of()).size()))
                        .toList())
                .build();
    }

    // ==================== Aturan status & hitungan ====================

    private StatusTahunPemberdayaan tentukanStatus(int tahun, int tahunIni, boolean adaKegiatan) {
        if (adaKegiatan) {
            return StatusTahunPemberdayaan.TERLAKSANA;
        }
        return (tahun < tahunIni) ? StatusTahunPemberdayaan.TERLEWAT : StatusTahunPemberdayaan.BELUM_TERLAKSANA;
    }

    private long hitungPeserta(List<PemberdayaanKegiatan> daftar) {
        return daftar.stream().mapToLong(k -> k.getJumlahPeserta()).sum();
    }

    private int hitungFoto(List<PemberdayaanKegiatan> daftar, Map<Long, List<PemberdayaanFotoResponseDto>> fotoPerKegiatan) {
        return daftar.stream()
                .mapToInt(k -> fotoPerKegiatan.getOrDefault(k.getId(), List.of()).size())
                .sum();
    }

    private int hitungLaporan(List<PemberdayaanKegiatan> daftar) {
        return (int) daftar.stream().filter(k -> k.getLaporanPath() != null).count();
    }

    private PemberdayaanRekapKegiatanDto toRekapKegiatan(PemberdayaanKegiatan entity, int jumlahFoto) {
        return PemberdayaanRekapKegiatanDto.builder()
                .id(entity.getId())
                .namaKegiatan(entity.getNamaKegiatan())
                .tanggal(entity.getTanggal())
                .hari(NAMA_HARI[entity.getTanggal().getDayOfWeek().getValue() - 1])
                .waktu(entity.getWaktu().format(FORMAT_WAKTU))
                .tempat(entity.getTempat())
                .jumlahPeserta(entity.getJumlahPeserta())
                .jumlahFoto(jumlahFoto)
                .adaLaporan(entity.getLaporanPath() != null)
                .build();
    }
}