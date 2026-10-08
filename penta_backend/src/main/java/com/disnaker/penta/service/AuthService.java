package com.disnaker.penta.service;

import com.disnaker.penta.dto.LoginRequestDto;
import com.disnaker.penta.dto.LoginResponseDto;
import com.disnaker.penta.entity.User;
import com.disnaker.penta.exception.UnauthorizedException;
import com.disnaker.penta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private static final String PESAN_LOGIN_GAGAL = "Username atau password salah";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    public LoginResponseDto login(LoginRequestDto request) {
        String username = request.getUsername().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException(PESAN_LOGIN_GAGAL));

        // Pesan sengaja sama untuk "username tidak ada" dan "password salah"
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(PESAN_LOGIN_GAGAL);
        }

        // Status nonaktif baru dibuka setelah password benar
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new UnauthorizedException("Akun Anda nonaktif, silakan hubungi admin");
        }

        return LoginResponseDto.builder()
                .token(jwtService.generateToken(user))
                .tokenType("Bearer")
                .expiresInSeconds(jwtService.getExpiresInSeconds())
                .user(userService.toResponseDto(user))
                .build();
    }
}