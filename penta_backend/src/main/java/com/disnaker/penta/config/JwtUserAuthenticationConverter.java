package com.disnaker.penta.config;

import com.disnaker.penta.entity.User;
import com.disnaker.penta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Dijalankan di SETIAP request yang membawa token valid.
 * Role dan status aktif dibaca dari DATABASE (bukan dari isi token), jadi:
 * - akun yang dinonaktifkan langsung ditolak di request berikutnya,
 * - perubahan role langsung berlaku tanpa menunggu token kedaluwarsa.
 */
@Component
@RequiredArgsConstructor
public class JwtUserAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Long userId;
        try {
            userId = Long.valueOf(jwt.getSubject());
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("Token tidak valid");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("Akun tidak ditemukan"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new DisabledException("Akun nonaktif");
        }

        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return new JwtAuthenticationToken(jwt, authorities, String.valueOf(user.getId()));
    }
}