package com.disnaker.penta.repository;

import com.disnaker.penta.entity.KomunitasFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface KomunitasFotoRepository extends JpaRepository<KomunitasFoto, Long> {

    List<KomunitasFoto> findByKegiatanIdOrderByIdAsc(Long kegiatanId);

    List<KomunitasFoto> findByKegiatanIdInOrderByIdAsc(Collection<Long> kegiatanIds);

    long countByKegiatanId(Long kegiatanId);
}