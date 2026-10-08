package com.disnaker.penta.repository;

import com.disnaker.penta.entity.KomunitasKegiatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KomunitasKegiatanRepository extends JpaRepository<KomunitasKegiatan, Long> {

    /** Semua tahun. Cari di nama kegiatan atau tempat (kata kunci kosong = semua data). Terbaru di atas. */
    @Query("""
            SELECT k FROM KomunitasKegiatan k
            WHERE LOWER(k.namaKegiatan) LIKE LOWER(CONCAT('%', :cari, '%'))
               OR LOWER(k.tempat) LIKE LOWER(CONCAT('%', :cari, '%'))
            ORDER BY k.tanggal DESC, k.waktu DESC
            """)
    List<KomunitasKegiatan> cariSemua(@Param("cari") String cari);

    /** Sama seperti di atas, tapi dibatasi satu tahun */
    @Query("""
            SELECT k FROM KomunitasKegiatan k
            WHERE YEAR(k.tanggal) = :tahun
              AND (LOWER(k.namaKegiatan) LIKE LOWER(CONCAT('%', :cari, '%'))
                   OR LOWER(k.tempat) LIKE LOWER(CONCAT('%', :cari, '%')))
            ORDER BY k.tanggal DESC, k.waktu DESC
            """)
    List<KomunitasKegiatan> cariPerTahun(@Param("tahun") int tahun, @Param("cari") String cari);
}