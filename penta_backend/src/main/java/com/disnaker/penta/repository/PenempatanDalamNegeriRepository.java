package com.disnaker.penta.repository;

import com.disnaker.penta.entity.PenempatanDalamNegeri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenempatanDalamNegeriRepository extends JpaRepository<PenempatanDalamNegeri, Long> {

    List<PenempatanDalamNegeri> findByNamaContainingIgnoreCase(String nama);

    List<PenempatanDalamNegeri> findByNikContaining(String nik);

    List<PenempatanDalamNegeri> findByTahunAndBulan(Integer tahun, Integer bulan);
}