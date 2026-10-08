package com.disnaker.penta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Untuk kartu ringkasan di halaman Manajemen Pengguna */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRingkasanResponseDto {

    private long totalAdmin;
    private long totalPegawai;
    private long akunAktif;
    private long totalAkun;
}