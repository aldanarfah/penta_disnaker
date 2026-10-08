package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * pendidikanTerakhir, provinsi, dan kabupatenKota (domisili alumni) SENGAJA TIDAK ADA di sini.
 * Ketiganya selalu tetap: SMA/SMK, Jawa Timur, Lumajang — diisi otomatis oleh Service
 * dan tidak bisa ditimpa oleh client, karena BKK memang khusus alumni SMK di Lumajang.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BkkPenempatanRequestDto {

    @NotNull(message = "Sekolah wajib dipilih")
    private Long sekolahId;

    @Size(min = 16, max = 16, message = "NIK harus 16 digit")
    private String nik;

    @NotBlank(message = "Nama wajib diisi")
    @Size(max = 150, message = "Nama maksimal 150 karakter")
    private String nama;

    private JenisKelamin jenisKelamin;

    private String kecamatan;

    private String desaKelurahan;

    private String alamat;

    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email maksimal 100 karakter")
    private String email;

    @Size(max = 20, message = "Nomor HP maksimal 20 karakter")
    private String noHp;

    private String provinsiPenempatan;

    private String kabupatenKotaPenempatan;

    private String namaPerusahaan;

    @Size(max = 50, message = "NIB maksimal 50 karakter")
    private String nibPerusahaan;

    private String alamatPerusahaan;

    private String provinsiPerusahaan;

    private String kabupatenKotaPerusahaan;

    private String jabatan;

    private String lapanganUsaha;

    private LocalDate tglMulai;

    private BigDecimal gajiUpah;
}