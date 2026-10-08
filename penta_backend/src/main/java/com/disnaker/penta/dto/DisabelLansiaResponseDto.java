package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
import com.disnaker.penta.entity.enums.StatusKerjaLansia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO untuk data yang dikirim SERVER ke client (hasil GET/POST/PUT).
 * Berisi id dan timestamp yang dibuat oleh server.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisabelLansiaResponseDto {

    private Long id;

    private String namaPerusahaan;

    private String nama;

    private String nik;

    private String tempatLahir;

    private LocalDate tanggalLahir;

    private String alamat;

    private String provinsi;

    private String kabupatenKota;

    private String noHp;

    private String email;

    private String keahlian;

    private String sertifikatKompetensi;

    private String pengalamanKerja;

    private PendidikanTerakhir pendidikanTerakhir;

    private JenisKelamin jenisKelamin;

    private StatusKerjaLansia statusKerja;

    private LocalDate tmtPenempatan;

    private String jabatan;

    private String statusKepegawaian;

    private String sektorUsaha;

    private String hambatan;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}