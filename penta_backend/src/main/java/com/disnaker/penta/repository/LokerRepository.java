package com.disnaker.penta.repository;

import com.disnaker.penta.entity.Loker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LokerRepository extends JpaRepository<Loker, Long> {

    List<Loker> findByNamaPerusahaanContainingIgnoreCase(String namaPerusahaan);

    List<Loker> findByNamaJabatanContainingIgnoreCase(String namaJabatan);

    List<Loker> findByTahunAndBulan(Integer tahun, Integer bulan);
}