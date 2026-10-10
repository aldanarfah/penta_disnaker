package com.disnaker.penta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PemberdayaanResponseDto {

    private Long id;
    private String namaKegiatan;
    private LocalDate tanggal;
    /** Senin sampai Minggu, dihitung otomatis dari tanggal */
    private String hari;
    /** Format "HH:mm" */
    private String waktu;
    private String tempat;
    private Integer jumlahPeserta;

    /** Foto dokumentasi (0 sampai 2), urut dari yang pertama diunggah */
    private List<PemberdayaanFotoResponseDto> foto;

    /** Semua field laporan bernilai null kalau laporan belum diunggah */
    private String laporanNama;
    /** Ukuran laporan dalam byte */
    private Long laporanUkuran;
    private String laporanUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}