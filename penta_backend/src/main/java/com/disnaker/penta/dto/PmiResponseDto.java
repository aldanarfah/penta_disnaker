package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.StatusPmi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PmiResponseDto {

    private Long id;
    private String nama;
    private String nik;
    private String noPaspor;
    private StatusPmi statusPmi;
    private String tempatLahir;
    private LocalDate tanggalLahir;
    private String alamat;
    private String permasalahan;
    private String negara;
    private JenisKelamin jenisKelamin;
    private LocalDate tanggalPemulangan;
    private String keterangan;
    private String foto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
