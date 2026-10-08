package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelaminLoker;
import com.disnaker.penta.entity.enums.PendidikanBps;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LokerRequestDto {

    @Min(value = 1, message = "Bulan harus antara 1-12")
    @Max(value = 12, message = "Bulan harus antara 1-12")
    private Integer bulan;

    private Integer tahun;

    @NotBlank(message = "Nama perusahaan wajib diisi")
    @Size(max = 150, message = "Nama perusahaan maksimal 150 karakter")
    private String namaPerusahaan;

    @Size(max = 50, message = "NIB maksimal 50 karakter")
    private String nibPerusahaan;

    private String lapanganUsaha;

    private String alamatPerusahaan;

    private String desa;

    private String kecamatan;

    private String provinsiPerusahaan;

    private String kabupatenKotaPerusahaan;

    @Size(max = 20, message = "Kode jabatan maksimal 20 karakter")
    private String kodeJabatan;

    private String namaJabatan;

    @Min(value = 0, message = "Jumlah dibutuhkan tidak boleh negatif")
    private Integer jumlahDibutuhkan;

    private JenisKelaminLoker jenisKelamin;

    private PendidikanBps pendidikan;

    private String keterampilanKompetensi;
}