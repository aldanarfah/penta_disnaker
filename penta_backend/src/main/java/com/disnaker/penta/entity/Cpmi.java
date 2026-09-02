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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cpmi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cpmi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tanggal_rekom")
    private LocalDate tanggalRekom;

    @Column(name = "nama", length = 150, nullable = false)
    private String nama;

    @Column(name = "tempat_lahir", length = 100)
    private String tempatLahir;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "desa_kelurahan", length = 100)
    private String desaKelurahan;

    @Column(name = "kecamatan", length = 100)
    private String kecamatan;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @Column(name = "pendidikan_terakhir")
    private PendidikanTerakhir pendidikanTerakhir;

    @Column(name = "jabatan", length = 100)
    private String jabatan;

    @Column(name = "negara_tujuan", length = 100)
    private String negaraTujuan;

    /** PPTKIS - Perusahaan Penempatan Pekerja Migran Indonesia Swasta */
    @Column(name = "perusahaan_pengirim", length = 150)
    private String perusahaanPengirim;

    /** Agensi/pemberi kerja di negara tujuan */
    @Column(name = "pemberi_kerja", length = 150)
    private String pemberiKerja;

    @Column(name = "no_hp", length = 20)
    private String noHp;

    @Column(name = "nik", length = 16)
    private String nik;

    @Column(name = "no_sertifikat_kompetensi", length = 50)
    private String noSertifikatKompetensi;

    @Column(name = "no_reg", length = 50)
    private String noReg;

    @Column(name = "bidang", length = 100)
    private String bidang;

    @Column(name = "kualifikasi_kompetensi", length = 150)
    private String kualifikasiKompetensi;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
