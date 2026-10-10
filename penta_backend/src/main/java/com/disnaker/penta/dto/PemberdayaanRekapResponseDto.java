package com.disnaker.penta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PemberdayaanRekapResponseDto {

    /** Tahun yang dipilih (untuk empat angka di bawah dan daftar kegiatan) */
    private int tahun;
    private int totalKegiatan;
    private long totalPeserta;
    private int totalFoto;
    private int laporanTerunggah;

    /** Jumlah tahun dalam tabel rekap per tahun (dari tahun pertama yang punya data sampai tahun ini) */
    private int tahunBerjalan;
    /** Jumlah tahun di antaranya yang punya minimal satu kegiatan */
    private int tahunTerlaksana;

    /** Tahun terbaru di atas. Bisa dipakai juga sebagai pilihan dropdown tahun. */
    private List<PemberdayaanRekapTahunDto> rekapPerTahun;

    /** Kegiatan pada tahun yang dipilih, terbaru di atas */
    private List<PemberdayaanRekapKegiatanDto> kegiatan;
}