package com.disnaker.penta.repository;

import com.disnaker.penta.entity.BkkPenempatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BkkPenempatanRepository extends JpaRepository<BkkPenempatan, Long> {

    List<BkkPenempatan> findByNamaContainingIgnoreCase(String nama);

    List<BkkPenempatan> findByNikContaining(String nik);

    List<BkkPenempatan> findBySekolahId(Long sekolahId);
}