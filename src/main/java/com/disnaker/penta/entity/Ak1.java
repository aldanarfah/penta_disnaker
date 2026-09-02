package com.disnaker.penta.entity;

import com.disnaker.penta.entity.enums.JenisKelamin;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
import com.disnaker.penta.entity.enums.StatusPerkawinan;
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
@Table(name = "ak1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ak1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "no_ak1", length = 30)
    private String noAk1;

    @Column(name = "nama", length = 150, nullable = false)
    private String nama;

    @Column(name = "nik", length = 16)
    private String nik;

    @Column(name = "tanggal_terdaftar")
    private LocalDate tanggalTerdaftar;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    @Column(name = "pendidikan_terakhir")
    private PendidikanTerakhir pendidikanTerakhir;

    @Column(name = "jurusan", length = 100)
    private String jurusan;

    @Column(name = "tahun_lulus")
       private Integer tahunLulus;

    @Column(name = "kecamatan", length = 100)
    private String kecamatan;

    @Column(name = "desa_kelurahan", length = 100)
    private String desaKelurahan;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "no_hp", length = 20)
    private String noHp;

    @Column(name = "tempat_lahir", length = 100)
    private String tempatLahir;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_perkawinan")
    private StatusPerkawinan statusPerkawinan;

    @Column(name = "tujuan_minat", length = 150)
    private String tujuanMinat;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
