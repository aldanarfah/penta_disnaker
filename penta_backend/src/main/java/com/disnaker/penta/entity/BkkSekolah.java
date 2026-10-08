package com.disnaker.penta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bkk_sekolah")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BkkSekolah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_sekolah", length = 150, nullable = false)
    private String namaSekolah;

    /** Path file SK STD, diisi lewat endpoint upload, bukan lewat create/update biasa */
    @Column(name = "sk_std", length = 255)
    private String skStd;

    /** Path file SK PAK, diisi lewat endpoint upload, bukan lewat create/update biasa */
    @Column(name = "sk_pak", length = 255)
    private String skPak;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}