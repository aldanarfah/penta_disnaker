package com.disnaker.penta.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data kegiatan yang dikirim CLIENT (create & update).
 * Tidak ada field hari (dihitung otomatis dari tanggal) dan tidak ada field foto/laporan
 * (diunggah lewat endpoint terpisah).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PemberdayaanRequestDto {

    @NotBlank(message = "Nama kegiatan wajib diisi")
    @Size(max = 200, message = "Nama kegiatan maksimal 200 karakter")
    private String namaKegiatan;

    @NotNull(message = "Tanggal wajib diisi")
    private LocalDate tanggal;

    /** Format "HH:mm" (contoh "09:30") */
    @NotNull(message = "Waktu wajib diisi")
    private LocalTime waktu;

    @NotBlank(message = "Tempat wajib diisi")
    @Size(max = 200, message = "Tempat maksimal 200 karakter")
    private String tempat;

    @NotNull(message = "Jumlah peserta wajib diisi")
    @Min(value = 1, message = "Jumlah peserta minimal 1")
    @Max(value = 10000, message = "Jumlah peserta maksimal 10.000")
    private Integer jumlahPeserta;
}