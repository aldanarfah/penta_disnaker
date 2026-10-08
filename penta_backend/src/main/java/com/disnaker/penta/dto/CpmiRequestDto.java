package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
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
public class CpmiRequestDto {

    private LocalDate tanggalRekom;

    @NotBlank(message = "Nama wajib diisi")
    @Size(max = 150, message = "Nama maksimal 150 karakter")
    private String nama;

    private String tempatLahir;

    private LocalDate tanggalLahir;

    private String alamat;

    private String desaKelurahan;

    private String kecamatan;

    private JenisKelamin jenisKelamin;

    private PendidikanTerakhir pendidikanTerakhir;

    private String jabatan;

    private String negaraTujuan;

    /** PPTKIS - Perusahaan Penempatan Pekerja Migran Indonesia Swasta */
    private String perusahaanPengirim;

    /** Agensi/pemberi kerja di negara tujuan */
    private String pemberiKerja;

    private String noHp;

    @Size(min = 16, max = 16, message = "NIK harus 16 digit")
    private String nik;

    @Size(max = 50, message = "Nomor sertifikat kompetensi maksimal 50 karakter")
    private String noSertifikatKompetensi;

    @Size(max = 50, message = "Nomor registrasi maksimal 50 karakter")
    private String noReg;

    private String bidang;

    private String kualifikasiKompetensi;

    @Size(max = 255, message = "Keterangan maksimal 255 karakter")
    private String keterangan;
}