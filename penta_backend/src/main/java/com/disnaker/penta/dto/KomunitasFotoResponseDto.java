package com.disnaker.penta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KomunitasFotoResponseDto {
    private Long id;
    private Long kegiatanId;
    private String namaFile;
    private LocalDateTime uploadedAt;
    /** Alamat untuk mengambil gambar (wajib kirim header Authorization) */
    private String url;
}