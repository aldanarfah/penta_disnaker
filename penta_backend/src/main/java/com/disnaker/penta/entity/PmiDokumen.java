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
 * Dokumen pendukung untuk satu data PMI Bermasalah (paspor, kontrak kerja, dll).
 * Satu Pmi bisa punya banyak PmiDokumen (relasi one-to-many).
 */
@Entity
@Table(name = "pmi_dokumen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PmiDokumen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pmi_id", nullable = false)
    private Pmi pmi;

    @Column(name = "nama_file", length = 255, nullable = false)
    private String namaFile;

    @Column(name = "path_file", length = 500, nullable = false)
    private String pathFile;

    /** Misal: Paspor, Kontrak Kerja, Surat Pengaduan */
    @Column(name = "jenis_dokumen", length = 100)
    private String jenisDokumen;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;
}