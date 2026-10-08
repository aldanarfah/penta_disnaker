package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanBps;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenempatanDalamNegeriRequestDto {

    @Min(value = 1, message = "Bulan harus antara 1-12")
    @Max(value = 12, message = "Bulan harus antara 1-12")
    private Integer bulan;

    private Integer tahun;

    @NotBlank(message = "Nama wajib diisi")
    @Size(max = 150, message = "Nama maksimal 150 karakter")
    private String nama;

    @Size(min = 16, max = 16, message = "NIK harus 16 digit")
    private String nik;

    private String alamat;

    private String desa;

    private String kecamatan;

    private String provinsi;

    private String kabupatenKota;

    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email maksimal 100 karakter")
    private String email;

    @Size(max = 20, message = "Nomor HP maksimal 20 karakter")
    private String noHp;

    private JenisKelamin jenisKelamin;

    private PendidikanBps pendidikan;

    private String provinsiPenempatan;

    private String kabupatenKotaPenempatan;

    private String namaPerusahaan;

    @Size(max = 50, message = "NIB maksimal 50 karakter")
    private String nibPerusahaan;

    private String alamatPerusahaan;

    private String provinsiPerusahaan;

    private String kabupatenKotaPerusahaan;

    private String namaJabatan;

    private String lapanganUsaha;

    private LocalDate tanggalMulai;

    private BigDecimal gajiUpah;
}