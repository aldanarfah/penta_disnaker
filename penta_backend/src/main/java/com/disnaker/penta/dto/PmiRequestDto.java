package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.StatusPmi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Field "foto" SENGAJA TIDAK ADA di sini. Foto hanya bisa diisi lewat
 * endpoint upload (POST /api/pmi/{id}/upload-foto), bukan lewat create/update biasa.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PmiRequestDto {

    @NotBlank(message = "Nama wajib diisi")
    @Size(max = 150, message = "Nama maksimal 150 karakter")
    private String nama;

    @Size(min = 16, max = 16, message = "NIK harus 16 digit")
    private String nik;

    @Size(max = 20, message = "Nomor paspor maksimal 20 karakter")
    private String noPaspor;

    private StatusPmi statusPmi;

    private String tempatLahir;

    private LocalDate tanggalLahir;

    private String alamat;

    private String permasalahan;

    private String negara;

    private JenisKelamin jenisKelamin;

    private LocalDate tanggalPemulangan;

    private String keterangan;
}