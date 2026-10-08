package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
import com.disnaker.penta.entity.enums.StatusPerkawinan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO untuk data yang dikirim CLIENT ke server (create & update).
 * Tidak ada field id/createdAt/updatedAt karena itu ditentukan server.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ak1RequestDto {

    @Size(max = 30, message = "Nomor AK1 maksimal 30 karakter")
    private String noAk1;

    @NotBlank(message = "Nama wajib diisi")
    @Size(max = 150, message = "Nama maksimal 150 karakter")
    private String nama;

    @Size(min = 16, max = 16, message = "NIK harus 16 digit")
    private String nik;

    private LocalDate tanggalTerdaftar;

    private String email;

    private JenisKelamin jenisKelamin;

    private PendidikanTerakhir pendidikanTerakhir;

    private String jurusan;

    private Integer tahunLulus;

    private String kecamatan;

    private String desaKelurahan;

    private String alamat;

    private String noHp;

    private String tempatLahir;

    private LocalDate tanggalLahir;

    private StatusPerkawinan statusPerkawinan;

    private String tujuanMinat;
}
