package com.disnaker.penta.repository;

import com.disnaker.penta.entity.User;
import com.disnaker.penta.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNip(String nip);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByNipAndIdNot(String nip, Long id);

    long countByRole(UserRole role);

    long countByIsActive(Boolean isActive);

    /** Pencarian satu kotak: cocok di nama, NIP/ID, atau username (tidak peduli huruf besar/kecil) */
    @Query("""
            SELECT u FROM User u
            WHERE LOWER(u.namaLengkap) LIKE LOWER(CONCAT('%', :cari, '%'))
               OR LOWER(u.nip) LIKE LOWER(CONCAT('%', :cari, '%'))
               OR LOWER(u.username) LIKE LOWER(CONCAT('%', :cari, '%'))
            ORDER BY u.id
            """)
    List<User> cari(@Param("cari") String cari);
}