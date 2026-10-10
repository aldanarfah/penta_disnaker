package com.disnaker.penta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Satu kegiatan pemberdayaan penyandang disabilitas.
 * Hari TIDAK disimpan, dihitung dari tanggal saat membuat response.
 */
@Entity
@Table(name = "pemberdayaan_kegiatan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PemberdayaanKegiatan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_kegiatan", length = 200, nullable = false)
    private String namaKegiatan;

    @Column(name = "tanggal", nullable = false)
    private LocalDate tanggal;

    @Column(name = "waktu", nullable = false)
    private LocalTime waktu;

    @Column(name = "tempat", length = 200, nullable = false)
    private String tempat;

    @Column(name = "jumlah_peserta", nullable = false)
    private Integer jumlahPeserta;

    /** Path file laporan, diisi lewat endpoint upload, bukan lewat create/update biasa */
    @Column(name = "laporan_path", length = 255)
    private String laporanPath;

    /** Nama asli file laporan (untuk ditampilkan dan nama saat diunduh) */
    @Column(name = "laporan_nama", length = 255)
    private String laporanNama;

    /** Ukuran file laporan dalam byte */
    @Column(name = "laporan_ukuran")
    private Long laporanUkuran;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}