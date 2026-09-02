package com.disnaker.penta.entity;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
import com.disnaker.penta.entity.enums.StatusKerjaLansia;
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
@Table(name = "disabel_lansia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisabelLansia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_perusahaan", length = 150)
    private String namaPerusahaan;

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

    @Column(name = "provinsi", length = 100)
    private String provinsi;

    @Column(name = "kabupaten_kota", length = 100)
    private String kabupatenKota;

    @Column(name = "no_hp", length = 20)
    private String noHp;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "keahlian", length = 150)
    private String keahlian;

    @Column(name = "sertifikat_kompetensi", length = 150)
    private String sertifikatKompetensi;

    @Column(name = "pengalaman_kerja", length = 255)
    private String pengalamanKerja;

    @Column(name = "pendidikan_terakhir")
    private PendidikanTerakhir pendidikanTerakhir;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_kerja")
    private StatusKerjaLansia statusKerja;

    @Column(name = "tmt_penempatan")
    private LocalDate tmtPenempatan;

    @Column(name = "jabatan", length = 100)
    private String jabatan;

    /** PKWT / PKWTT */
    @Column(name = "status_kepegawaian", length = 30)
    private String statusKepegawaian;

    @Column(name = "sektor_usaha", length = 150)
    private String sektorUsaha;

    @Column(name = "hambatan", length = 255)
    private String hambatan;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
