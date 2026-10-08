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
public class KomunitasRekapResponseDto {

    private int tahun;
    /** Target kegiatan per tahun (satu bulan minimal satu kegiatan), selalu 12 */
    private int targetBulan;
    /** Jumlah bulan yang sudah berjalan: tahun lalu = 12, tahun ini = bulan sekarang, tahun depan = 0 */
    private int bulanBerjalan;
    /** Jumlah bulan yang punya minimal satu kegiatan (satu bulan dihitung sekali walau kegiatannya dua) */
    private int bulanTerlaksana;
    /** bulanTerlaksana dibagi bulanBerjalan dalam persen (0-100) */
    private int capaianPersen;
    /** Jumlah bulan yang sudah lewat tanpa kegiatan */
    private int bulanTerlewat;
    private int totalKegiatan;
    private int totalFoto;
    private int laporanTerunggah;
    /** Selalu 12 elemen, Januari sampai Desember */
    private List<KomunitasRekapBulanDto> bulan;
}