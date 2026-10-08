package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Form "Edit Pegawai". Semua field dikirim sekaligus (PUT penuh).
 * Pengecualian: password boleh kosong/tidak dikirim, artinya password tidak diubah.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDto {

    @NotBlank(message = "Nama lengkap wajib diisi")
    @Size(max = 150, message = "Nama lengkap maksimal 150 karakter")
    private String namaLengkap;

    @NotBlank(message = "NIP / ID wajib diisi")
    @Size(max = 50, message = "NIP / ID maksimal 50 karakter")
    private String nip;

    @NotNull(message = "Role wajib dipilih")
    private UserRole role;

    @NotBlank(message = "Username wajib diisi")
    @Size(min = 3, max = 50, message = "Username harus 3-50 karakter")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$",
            message = "Username hanya boleh berisi huruf, angka, titik, garis bawah, dan strip")
    private String username;

    /** Opsional: kosong = tidak diubah. Kalau diisi harus 8-72 karakter. */
    @Pattern(regexp = "^(.{8,72})?$", message = "Password baru harus 8-72 karakter")
    private String password;

    @NotNull(message = "Status akun wajib diisi")
    private Boolean isActive;
}