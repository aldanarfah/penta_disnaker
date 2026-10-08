package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
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
public class CpmiResponseDto {

    private Long id;

    private LocalDate tanggalRekom;

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

    private String perusahaanPengirim;

    private String pemberiKerja;

    private String noHp;

    private String nik;

    private String noSertifikatKompetensi;

    private String noReg;

    private String bidang;

    private String kualifikasiKompetensi;

    private String keterangan;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}