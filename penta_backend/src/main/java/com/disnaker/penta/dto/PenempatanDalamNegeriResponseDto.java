package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanBps;
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
public class PenempatanDalamNegeriResponseDto {

    private Long id;
    private Integer bulan;
    private Integer tahun;
    private String nama;
    private String nik;
    private String alamat;
    private String desa;
    private String kecamatan;
    private String provinsi;
    private String kabupatenKota;
    private String email;
    private String noHp;
    private JenisKelamin jenisKelamin;
    private PendidikanBps pendidikan;
    private String provinsiPenempatan;
    private String kabupatenKotaPenempatan;
    private String namaPerusahaan;
    private String nibPerusahaan;
    private String alamatPerusahaan;
    private String provinsiPerusahaan;
    private String kabupatenKotaPerusahaan;
    private String namaJabatan;
    private String lapanganUsaha;
    private LocalDate tanggalMulai;
    private BigDecimal gajiUpah;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}