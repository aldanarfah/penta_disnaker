package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
import com.disnaker.penta.entity.enums.RagamDisabilitas;
import com.disnaker.penta.entity.enums.StatusKerjaDisabilitas;
import jakarta.validation.constraints.Email;
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
public class DisabelPerusahaanRequestDto {

    private String namaPerusahaan;

    @NotBlank(message = "Nama wajib diisi")
    @Size(max = 150, message = "Nama maksimal 150 karakter")
    private String nama;

    @Size(min = 16, max = 16, message = "NIK harus 16 digit")
    private String nik;

    private String tempatLahir;

    private LocalDate tanggalLahir;

    private String alamat;

    private String provinsi;

    private String kabupatenKota;

    @Size(max = 20, message = "Nomor HP maksimal 20 karakter")
    private String noHp;

    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email maksimal 100 karakter")
    private String email;

    private String keahlian;

    private String sertifikatKompetensi;

    private String pengalamanKerja;

    private PendidikanTerakhir pendidikanTerakhir;

    private JenisKelamin jenisKelamin;

    private RagamDisabilitas ragamDisabilitas;

    private String spesifikRagam;

    private StatusKerjaDisabilitas statusKerja;

    private LocalDate tmtPenempatan;

    private String jabatan;

    /** PKWT / PKWTT */
    @Size(max = 30, message = "Status kepegawaian maksimal 30 karakter")
    private String statusKepegawaian;

    private String sektorUsaha;

    private String hambatan;
}