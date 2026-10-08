package com.disnaker.penta.entity;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
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
@Table(name = "bkk_penempatan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BkkPenempatan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Boleh NULL: kalau sekolah induknya dihapus, kolom ini otomatis menjadi NULL
     * (ON DELETE SET NULL di database), tapi baris data alumni ini tidak ikut terhapus.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sekolah_id")
    private BkkSekolah sekolah;

    @Column(name = "nik", length = 16)
    private String nik;

    @Column(name = "nama", length = 150, nullable = false)
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @Column(name = "kecamatan", length = 100)
    private String kecamatan;

    @Column(name = "desa_kelurahan", length = 100)
    private String desaKelurahan;

    @Column(name = "alamat", length = 255)
    private String alamat;

    /** Default "Jawa Timur" kalau tidak dikirim, diisi otomatis di Service */
    @Column(name = "provinsi", length = 100)
    private String provinsi;

    /** Default "Lumajang" kalau tidak dikirim, diisi otomatis di Service */
    @Column(name = "kabupaten_kota", length = 100)
    private String kabupatenKota;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "no_hp", length = 20)
    private String noHp;

    /** Default SMA_SMK kalau tidak dikirim, diisi otomatis di Service */
    @Column(name = "pendidikan_terakhir")
    private PendidikanTerakhir pendidikanTerakhir;

    @Column(name = "provinsi_penempatan", length = 100)
    private String provinsiPenempatan;

    @Column(name = "kabupaten_kota_penempatan", length = 100)
    private String kabupatenKotaPenempatan;

    @Column(name = "nama_perusahaan", length = 150)
    private String namaPerusahaan;

    /** Nomor Induk Berusaha */
    @Column(name = "nib_perusahaan", length = 50)
    private String nibPerusahaan;

    @Column(name = "alamat_perusahaan", length = 255)
    private String alamatPerusahaan;

    @Column(name = "provinsi_perusahaan", length = 100)
    private String provinsiPerusahaan;

    @Column(name = "kabupaten_kota_perusahaan", length = 100)
    private String kabupatenKotaPerusahaan;

    @Column(name = "jabatan", length = 100)
    private String jabatan;

    @Column(name = "lapangan_usaha", length = 150)
    private String lapanganUsaha;

    @Column(name = "tgl_mulai")
    private LocalDate tglMulai;

    @Column(name = "gaji_upah", precision = 15, scale = 2)
    private BigDecimal gajiUpah;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}