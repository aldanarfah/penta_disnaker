package com.disnaker.penta.repository;

import com.disnaker.penta.entity.BkkSekolah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BkkSekolahRepository extends JpaRepository<BkkSekolah, Long> {

    List<BkkSekolah> findByNamaSekolahContainingIgnoreCase(String namaSekolah);
}