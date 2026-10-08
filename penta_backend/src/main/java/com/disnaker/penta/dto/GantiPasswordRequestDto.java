package com.disnaker.penta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Form "Ganti Password Admin" (hanya untuk akun admin yang sedang login) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GantiPasswordRequestDto {

    @NotBlank(message = "Password saat ini wajib diisi")
    private String passwordSaatIni;

    @NotBlank(message = "Password baru wajib diisi")
    @Size(min = 8, max = 72, message = "Password baru harus 8-72 karakter")
    private String passwordBaru;
}