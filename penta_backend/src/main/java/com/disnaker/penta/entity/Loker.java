package com.disnaker.penta.entity;

import com.disnaker.penta.entity.enums.JenisKelaminLoker;
import com.disnaker.penta.entity.enums.PendidikanBps;
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
@Table(name = "loker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bulan")
    private Integer bulan;

    @Column(name = "tahun")
    private Integer tahun;

    @Column(name = "nama_perusahaan", length = 150, nullable = false)
    private String namaPerusahaan;

    @Column(name = "nib_perusahaan", length = 50)
    private String nibPerusahaan;

    @Column(name = "lapangan_usaha", length = 150)
    private String lapanganUsaha;

    @Column(name = "alamat_perusahaan", length = 255)
    private String alamatPerusahaan;

    @Column(name = "desa", length = 100)
    private String desa;

    @Column(name = "kecamatan", length = 100)
    private String kecamatan;

    @Column(name = "provinsi_perusahaan", length = 100)
    private String provinsiPerusahaan;

    @Column(name = "kabupaten_kota_perusahaan", length = 100)
    private String kabupatenKotaPerusahaan;

    /** Kode KBJI, diisi manual sebagai teks bebas (lihat sheet (MS) KODE KBJI di Excel sebagai referensi) */
    @Column(name = "kode_jabatan", length = 20)
    private String kodeJabatan;

    @Column(name = "nama_jabatan", length = 150)
    private String namaJabatan;

    @Column(name = "jumlah_dibutuhkan")
    private Integer jumlahDibutuhkan;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelaminLoker jenisKelamin;

    @Enumerated(EnumType.STRING)
    @Column(name = "pendidikan")
    private PendidikanBps pendidikan;

    @Column(name = "keterampilan_kompetensi", length = 255)
    private String keterampilanKompetensi;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}