package com.disnaker.penta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Foto dokumentasi satu kegiatan komunitas (maksimal 2 per kegiatan).
 * Satu KomunitasKegiatan bisa punya banyak KomunitasFoto (one-to-many).
 */
@Entity
@Table(name = "komunitas_foto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KomunitasFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kegiatan_id", nullable = false)
    private KomunitasKegiatan kegiatan;

    @Column(name = "nama_file", length = 255, nullable = false)
    private String namaFile;

    @Column(name = "path_file", length = 500, nullable = false)
    private String pathFile;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;
}