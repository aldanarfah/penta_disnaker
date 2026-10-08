package com.disnaker.penta.controller;

import com.disnaker.penta.config.CurrentUser;
import com.disnaker.penta.dto.ResetPasswordRequestDto;
import com.disnaker.penta.dto.UserCreateRequestDto;
import com.disnaker.penta.dto.UserResponseDto;
import com.disnaker.penta.dto.UserRingkasanResponseDto;
import com.disnaker.penta.dto.UserUpdateRequestDto;
import com.disnaker.penta.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Manajemen Pengguna", description = "Kelola akun admin dan pegawai (khusus ADMIN)")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Ambil semua akun, atau cari berdasarkan nama/NIP/username")
    public ResponseEntity<List<UserResponseDto>> getAll(
            @RequestParam(required = false) String cari) {

        if (cari != null && !cari.isBlank()) {
            return ResponseEntity.ok(userService.search(cari));
        }
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/ringkasan")
    @Operation(summary = "Angka ringkasan untuk kartu: total admin, total pegawai, akun aktif, total akun")
    public ResponseEntity<UserRingkasanResponseDto> ringkasan() {
        return ResponseEntity.ok(userService.ringkasan());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu akun berdasarkan id")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah akun baru (status otomatis aktif)")
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateRequestDto request) {
        UserResponseDto created = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui akun. Password boleh dikosongkan (tidak diubah)")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto request) {
        return ResponseEntity.ok(userService.update(id, request, CurrentUser.id()));
    }

    @PutMapping("/{id}/reset-password")
    @Operation(summary = "Admin mengatur password baru untuk akun lain (tombol Reset Pass)")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Long id,
            @Valid @RequestBody ResetPasswordRequestDto request) {
        userService.resetPassword(id, request, CurrentUser.id());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus akun (tidak bisa menghapus akun sendiri)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id, CurrentUser.id());
        return ResponseEntity.noContent().build();
    }
}