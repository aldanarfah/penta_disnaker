package com.disnaker.penta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BkkSekolahRequestDto {

    @NotBlank(message = "Nama sekolah wajib diisi")
    @Size(max = 150, message = "Nama sekolah maksimal 150 karakter")
    private String namaSekolah;
}