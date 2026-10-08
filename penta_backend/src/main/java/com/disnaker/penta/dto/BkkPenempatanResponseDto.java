package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BkkPenempatanResponseDto {

    private Long id;

    /** Null kalau sekolah sudah dihapus */
    private Long sekolahId;

    /** Null kalau sekolah sudah dihapus */
    private String namaSekolah;

    private String nik;
    private String nama;
    private JenisKelamin jenisKelamin;
    private String kecamatan;
    private String desaKelurahan;
    private String alamat;
    private String provinsi;
    private String kabupatenKota;
    private String email;
    private String noHp;
    private PendidikanTerakhir pendidikanTerakhir;
    private String provinsiPenempatan;
    private String kabupatenKotaPenempatan;
    private String namaPerusahaan;
    private String nibPerusahaan;
    private String alamatPerusahaan;
    private String provinsiPerusahaan;
    private String kabupatenKotaPerusahaan;
    private String jabatan;
    private String lapanganUsaha;
    private LocalDate tglMulai;
    private BigDecimal gajiUpah;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}