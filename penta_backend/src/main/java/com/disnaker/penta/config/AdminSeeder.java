package com.disnaker.penta.config;

import com.disnaker.penta.entity.User;
import com.disnaker.penta.entity.enums.UserRole;
import com.disnaker.penta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Membuat akun admin pertama SATU KALI, hanya kalau tabel users masih kosong.
 * Password diambil dari app.admin.password (application.properties lokal, tidak ikut Git).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }
        if (adminPassword == null || adminPassword.length() < 8) {
            log.warn("Tabel users kosong, tapi app.admin.password belum diisi (minimal 8 karakter). "
                    + "Akun admin pertama TIDAK dibuat.");
            return;
        }

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode(adminPassword))
                .namaLengkap("Administrator")
                .nip("ADMIN-001")
                .role(UserRole.ADMIN)
                .isActive(true)
                .build();
        userRepository.save(admin);
        log.info("Akun admin pertama dibuat. Username: admin");
    }
}