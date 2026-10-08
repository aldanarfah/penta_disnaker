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
public class BkkSekolahResponseDto {

    private Long id;
    private String namaSekolah;
    private String skStd;
    private String skPak;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}