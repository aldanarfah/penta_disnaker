package com.disnaker.penta.service;

import com.disnaker.penta.dto.GantiPasswordRequestDto;
import com.disnaker.penta.dto.ResetPasswordRequestDto;
import com.disnaker.penta.dto.UserCreateRequestDto;
import com.disnaker.penta.dto.UserResponseDto;
import com.disnaker.penta.dto.UserRingkasanResponseDto;
import com.disnaker.penta.dto.UserUpdateRequestDto;
import com.disnaker.penta.entity.User;
import com.disnaker.penta.entity.enums.UserRole;
import com.disnaker.penta.exception.BusinessRuleException;
import com.disnaker.penta.exception.DuplicateResourceException;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * Catatan aturan "minimal harus ada satu admin aktif":
 * semua operasi di sini hanya bisa dilakukan oleh admin yang AKTIF (dijaga SecurityConfig).
 * Karena admin tidak boleh menghapus/menonaktifkan/menurunkan role akunnya sendiri,
 * si admin yang sedang login selalu tetap ada, jadi aturan itu otomatis terpenuhi.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDto> findAll() {
        return userRepository.findAll(Sort.by("id"))
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    /** Cari di nama, NIP/ID, atau username sekaligus */
    public List<UserResponseDto> search(String cari) {
        return userRepository.cari(cari.trim())
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public UserRingkasanResponseDto ringkasan() {
        return UserRingkasanResponseDto.builder()
                .totalAdmin(userRepository.countByRole(UserRole.ADMIN))
                .totalPegawai(userRepository.countByRole(UserRole.PEGAWAI))
                .akunAktif(userRepository.countByIsActive(true))
                .totalAkun(userRepository.count())
                .build();
    }

    public UserResponseDto findById(Long id) {
        return toResponseDto(cariAtauGagal(id));
    }

    public UserResponseDto create(UserCreateRequestDto request) {
        String username = normalisasiUsername(request.getUsername());
        String nip = request.getNip().trim();

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username '" + username + "' sudah dipakai");
        }
        if (userRepository.existsByNip(nip)) {
            throw new DuplicateResourceException("NIP / ID '" + nip + "' sudah terdaftar");
        }

        User entity = User.builder()
                .namaLengkap(request.getNamaLengkap().trim())
                .nip(nip)
                .username(username)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();
        return toResponseDto(userRepository.save(entity));
    }

    public UserResponseDto update(Long id, UserUpdateRequestDto request, Long currentUserId) {
        User existing = cariAtauGagal(id);

        String username = normalisasiUsername(request.getUsername());
        String nip = request.getNip().trim();

        if (userRepository.existsByUsernameAndIdNot(username, id)) {
            throw new DuplicateResourceException("Username '" + username + "' sudah dipakai");
        }
        if (userRepository.existsByNipAndIdNot(nip, id)) {
            throw new DuplicateResourceException("NIP / ID '" + nip + "' sudah terdaftar");
        }

        boolean adaPasswordBaru = request.getPassword() != null && !request.getPassword().isBlank();

        if (existing.getId().equals(currentUserId)) {
            boolean aktifSekarang = Boolean.TRUE.equals(existing.getIsActive());
            if (request.getRole() != existing.getRole()
                    || request.getIsActive().booleanValue() != aktifSekarang) {
                throw new BusinessRuleException(
                        "Anda tidak bisa mengubah role atau menonaktifkan akun Anda sendiri");
            }
            if (adaPasswordBaru) {
                throw new BusinessRuleException(
                        "Untuk mengganti password akun Anda sendiri, gunakan form Ganti Password");
            }
        }

        existing.setNamaLengkap(request.getNamaLengkap().trim());
        existing.setNip(nip);
        existing.setUsername(username);
        existing.setRole(request.getRole());
        existing.setIsActive(request.getIsActive());
        if (adaPasswordBaru) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return toResponseDto(userRepository.save(existing));
    }

    public void resetPassword(Long id, ResetPasswordRequestDto request, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new BusinessRuleException(
                    "Untuk mengganti password akun Anda sendiri, gunakan form Ganti Password");
        }
        User existing = cariAtauGagal(id);
        existing.setPassword(passwordEncoder.encode(request.getPasswordBaru()));
        userRepository.save(existing);
    }

    public void delete(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new BusinessRuleException("Anda tidak bisa menghapus akun Anda sendiri");
        }
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException.forId("Pengguna", id);
        }
        userRepository.deleteById(id);
    }

    /** Ganti password akun sendiri (dipakai admin yang sedang login) */
    public void gantiPasswordSendiri(Long currentUserId, GantiPasswordRequestDto request) {
        User user = cariAtauGagal(currentUserId);

        if (!passwordEncoder.matches(request.getPasswordSaatIni(), user.getPassword())) {
            throw new BusinessRuleException("Password saat ini salah");
        }
        if (request.getPasswordSaatIni().equals(request.getPasswordBaru())) {
            throw new BusinessRuleException("Password baru tidak boleh sama dengan password saat ini");
        }

        user.setPassword(passwordEncoder.encode(request.getPasswordBaru()));
        userRepository.save(user);
    }

    // ==================== Helper & Mapper ====================

    private User cariAtauGagal(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Pengguna", id));
    }

    /** Username selalu disimpan huruf kecil supaya "Budi" dan "budi" tidak jadi dua akun */
    private String normalisasiUsername(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }

    /** Public karena dipakai juga oleh AuthService. Password TIDAK ikut dipetakan. */
    public UserResponseDto toResponseDto(User entity) {
        return UserResponseDto.builder()
                .id(entity.getId())
                .namaLengkap(entity.getNamaLengkap())
                .nip(entity.getNip())
                .username(entity.getUsername())
                .role(entity.getRole())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}