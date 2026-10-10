package com.disnaker.penta.repository;

import com.disnaker.penta.entity.PemberdayaanFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PemberdayaanFotoRepository extends JpaRepository<PemberdayaanFoto, Long> {

    List<PemberdayaanFoto> findByKegiatanIdOrderByIdAsc(Long kegiatanId);

    List<PemberdayaanFoto> findByKegiatanIdInOrderByIdAsc(Collection<Long> kegiatanIds);

    long countByKegiatanId(Long kegiatanId);
}