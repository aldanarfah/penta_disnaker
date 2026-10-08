package com.disnaker.penta.service;

import com.disnaker.penta.dto.KomunitasFotoResponseDto;
import com.disnaker.penta.dto.KomunitasRekapBulanDto;
import com.disnaker.penta.dto.KomunitasRekapKegiatanDto;
import com.disnaker.penta.dto.KomunitasRekapResponseDto;
import com.disnaker.penta.entity.KomunitasKegiatan;
import com.disnaker.penta.entity.enums.StatusBulanKomunitas;
import com.disnaker.penta.exception.BusinessRuleException;
import com.disnaker.penta.repository.KomunitasKegiatanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KomunitasRekapService {

    private static final int TARGET_BULAN = 12;
    /** "Hari ini" dihitung berdasarkan jam server di zona waktu Lumajang */
    private static final ZoneId ZONA_SERVER = ZoneId.of("Asia/Jakarta");

    private static final String[] NAMA_BULAN = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    // Sama dengan yang dipakai KomunitasService (sengaja diulang supaya file yang sudah teruji tidak diubah)
    private static final String[] NAMA_HARI = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private static final DateTimeFormatter FORMAT_WAKTU = DateTimeFormatter.ofPattern("HH:mm");

    private final KomunitasKegiatanRepository kegiatanRepository;
    private final KomunitasFotoService fotoService;

    /** tahun kosong = tahun berjalan menurut jam server */
    public KomunitasRekapResponseDto rekap(Integer tahunParam) {
        LocalDate hariIni = LocalDate.now(ZONA_SERVER);
        int tahun = (tahunParam != null) ? tahunParam : hariIni.getYear();
        if (tahun < 2000 || tahun > 2100) {
            throw new BusinessRuleException("Tahun tidak valid (harus antara 2000 dan 2100)");
        }

        // Semua kegiatan setahun (kata kunci kosong = tanpa filter), diurutkan dari yang paling awal
        List<KomunitasKegiatan> daftar = kegiatanRepository.cariPerTahun(tahun, "").stream()
                .sorted(Comparator.comparing(KomunitasKegiatan::getTanggal)
                        .thenComparing(KomunitasKegiatan::getWaktu))
                .toList();

        List<Long> ids = daftar.stream().map(KomunitasKegiatan::getId).toList();
        Map<Long, List<KomunitasFotoResponseDto>> fotoPerKegiatan = fotoService.findByKegiatanIds(ids);
        Map<Integer, List<KomunitasKegiatan>> kegiatanPerBulan = daftar.stream()
                .collect(Collectors.groupingBy(k -> k.getTanggal().getMonthValue()));

        int bulanTerlaksana = 0;
        int bulanTerlewat = 0;
        List<KomunitasRekapBulanDto> daftarBulan = new ArrayList<>();

        for (int bulan = 1; bulan <= 12; bulan++) {
            List<KomunitasKegiatan> kegiatanBulanIni = kegiatanPerBulan.getOrDefault(bulan, List.of());
            StatusBulanKomunitas status = tentukanStatus(tahun, bulan, !kegiatanBulanIni.isEmpty(), hariIni);

            if (status == StatusBulanKomunitas.TERLAKSANA) {
                bulanTerlaksana++;
            } else if (status == StatusBulanKomunitas.TERLEWAT) {
                bulanTerlewat++;
            }

            daftarBulan.add(KomunitasRekapBulanDto.builder()
                    .bulan(bulan)
                    .namaBulan(NAMA_BULAN[bulan - 1])
                    .status(status)
                    .kegiatan(kegiatanBulanIni.stream()
                            .map(k -> toRekapKegiatan(k, fotoPerKegiatan.getOrDefault(k.getId(), List.of()).size()))
                            .toList())
                    .build());
        }

        int bulanBerjalan = hitungBulanBerjalan(tahun, hariIni);
        // Dibatasi maksimal 100 supaya aman kalau ada kegiatan yang dicatat di bulan yang belum tiba
        int capaianPersen = (bulanBerjalan == 0)
                ? 0
                : Math.min(100, (int) Math.round(bulanTerlaksana * 100.0 / bulanBerjalan));

        return KomunitasRekapResponseDto.builder()
                .tahun(tahun)
                .targetBulan(TARGET_BULAN)
                .bulanBerjalan(bulanBerjalan)
                .bulanTerlaksana(bulanTerlaksana)
                .capaianPersen(capaianPersen)
                .bulanTerlewat(bulanTerlewat)
                .totalKegiatan(daftar.size())
                .totalFoto(fotoPerKegiatan.values().stream().mapToInt(List::size).sum())
                .laporanTerunggah((int) daftar.stream().filter(k -> k.getLaporanPath() != null).count())
                .bulan(daftarBulan)
                .build();
    }

    // ==================== Aturan status ====================

    private StatusBulanKomunitas tentukanStatus(int tahun, int bulan, boolean adaKegiatan, LocalDate hariIni) {
        if (adaKegiatan) {
            return StatusBulanKomunitas.TERLAKSANA;
        }
        if (tahun < hariIni.getYear()) {
            return StatusBulanKomunitas.TERLEWAT;
        }
        if (tahun > hariIni.getYear()) {
            return StatusBulanKomunitas.BELUM_TIBA;
        }
        if (bulan < hariIni.getMonthValue()) {
            return StatusBulanKomunitas.TERLEWAT;
        }
        if (bulan == hariIni.getMonthValue()) {
            return StatusBulanKomunitas.BELUM_TERLAKSANA;
        }
        return StatusBulanKomunitas.BELUM_TIBA;
    }

    private int hitungBulanBerjalan(int tahun, LocalDate hariIni) {
        if (tahun < hariIni.getYear()) {
            return 12;
        }
        if (tahun == hariIni.getYear()) {
            return hariIni.getMonthValue();
        }
        return 0;
    }

    private KomunitasRekapKegiatanDto toRekapKegiatan(KomunitasKegiatan entity, int jumlahFoto) {
        return KomunitasRekapKegiatanDto.builder()
                .id(entity.getId())
                .namaKegiatan(entity.getNamaKegiatan())
                .tanggal(entity.getTanggal())
                .hari(NAMA_HARI[entity.getTanggal().getDayOfWeek().getValue() - 1])
                .waktu(entity.getWaktu().format(FORMAT_WAKTU))
                .tempat(entity.getTempat())
                .jumlahFoto(jumlahFoto)
                .adaLaporan(entity.getLaporanPath() != null)
                .build();
    }
}