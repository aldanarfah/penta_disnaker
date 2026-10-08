package com.disnaker.penta.controller;

import com.disnaker.penta.config.CurrentUser;
import com.disnaker.penta.dto.GantiPasswordRequestDto;
import com.disnaker.penta.dto.LoginRequestDto;
import com.disnaker.penta.dto.LoginResponseDto;
import com.disnaker.penta.dto.UserResponseDto;
import com.disnaker.penta.service.AuthService;
import com.disnaker.penta.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autentikasi", description = "Login dan profil akun yang sedang login")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login, mengembalikan token JWT (kirim di header Authorization: Bearer <token>)")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Data akun yang sedang login (nama, role, dst)")
    public ResponseEntity<UserResponseDto> me() {
        return ResponseEntity.ok(userService.findById(CurrentUser.id()));
    }

    @PutMapping("/ganti-password")
    @Operation(summary = "Ganti password akun sendiri (khusus ADMIN, wajib isi password saat ini)")
    public ResponseEntity<Void> gantiPassword(@Valid @RequestBody GantiPasswordRequestDto request) {
        userService.gantiPasswordSendiri(CurrentUser.id(), request);
        return ResponseEntity.noContent().build();
    }
}