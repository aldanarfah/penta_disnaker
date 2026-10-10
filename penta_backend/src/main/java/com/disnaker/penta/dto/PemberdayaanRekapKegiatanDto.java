package com.disnaker.penta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** Ringkasan satu kegiatan untuk tabel "Daftar kegiatan" di halaman rekap */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PemberdayaanRekapKegiatanDto {

    private Long id;
    private String namaKegiatan;
    private LocalDate tanggal;
    /** Senin sampai Minggu */
    private String hari;
    /** Format "HH:mm" */
    private String waktu;
    private String tempat;
    private Integer jumlahPeserta;
    private int jumlahFoto;
    /** true = laporan sudah diunggah */
    private boolean adaLaporan;
}