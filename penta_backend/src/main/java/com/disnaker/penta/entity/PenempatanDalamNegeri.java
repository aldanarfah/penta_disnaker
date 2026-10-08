package com.disnaker.penta.entity;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanBps;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "penempatan_dalam_negeri")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenempatanDalamNegeri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bulan")
    private Integer bulan;

    @Column(name = "tahun")
    private Integer tahun;

    @Column(name = "nama", length = 150, nullable = false)
    private String nama;

    @Column(name = "nik", length = 16)
    private String nik;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "desa", length = 100)
    private String desa;

    @Column(name = "kecamatan", length = 100)
    private String kecamatan;

    @Column(name = "provinsi", length = 100)
    private String provinsi;

    @Column(name = "kabupaten_kota", length = 100)
    private String kabupatenKota;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "no_hp", length = 20)
    private String noHp;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @Enumerated(EnumType.STRING)
    @Column(name = "pendidikan")
    private PendidikanBps pendidikan;

    @Column(name = "provinsi_penempatan", length = 100)
    private String provinsiPenempatan;

    @Column(name = "kabupaten_kota_penempatan", length = 100)
    private String kabupatenKotaPenempatan;

    @Column(name = "nama_perusahaan", length = 150)
    private String namaPerusahaan;

    @Column(name = "nib_perusahaan", length = 50)
    private String nibPerusahaan;

    @Column(name = "alamat_perusahaan", length = 255)
    private String alamatPerusahaan;

    @Column(name = "provinsi_perusahaan", length = 100)
    private String provinsiPerusahaan;

    @Column(name = "kabupaten_kota_perusahaan", length = 100)
    private String kabupatenKotaPerusahaan;

    @Column(name = "nama_jabatan", length = 100)
    private String namaJabatan;

    @Column(name = "lapangan_usaha", length = 150)
    private String lapanganUsaha;

    @Column(name = "tanggal_mulai")
    private LocalDate tanggalMulai;

    @Column(name = "gaji_upah", precision = 15, scale = 2)
    private BigDecimal gajiUpah;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}