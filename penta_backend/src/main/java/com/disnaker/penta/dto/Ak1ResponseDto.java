package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
import com.disnaker.penta.entity.enums.StatusPerkawinan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO untuk data yang dikirim server ke CLIENT.
 * Termasuk id, createdAt, updatedAt yang tidak ada di request DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ak1ResponseDto {

    private Long id;
    private String noAk1;
    private String nama;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
