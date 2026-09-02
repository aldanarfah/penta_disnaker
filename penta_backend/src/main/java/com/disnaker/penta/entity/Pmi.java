package com.disnaker.penta.entity;

import com.disnaker.penta.entity.enums.JenisKelamin;
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

@Entity
@Table(name = "pmi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pmi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama", length = 150, nullable = false)
    private String nama;

    @Column(name = "nik", length = 16)
    private String nik;

    @Column(name = "tempat_lahir", length = 100)
    private String tempatLahir;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "alamat", length = 255)
    private String alamat;

    /** Misal: Deportasi, Meninggal, Sakit, dll */
    @Column(name = "permasalahan", length = 150)
    private String permasalahan;

    @Column(name = "negara", length = 100)
    private String negara;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @Column(name = "tanggal_pemulangan")
    private LocalDate tanggalPemulangan;

    @Column(name = "keterangan", length = 255)
    private String keterangan;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
