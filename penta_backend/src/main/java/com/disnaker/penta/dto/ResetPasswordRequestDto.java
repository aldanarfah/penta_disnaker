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
public class ResetPasswordRequestDto {

    @NotBlank(message = "Password baru wajib diisi")
    @Size(min = 8, max = 72, message = "Password baru harus 8-72 karakter")
    private String passwordBaru;
}