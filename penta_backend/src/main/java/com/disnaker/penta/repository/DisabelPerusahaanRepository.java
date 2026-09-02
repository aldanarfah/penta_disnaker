package com.disnaker.penta.repository;

import com.disnaker.penta.entity.DisabelPerusahaan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisabelPerusahaanRepository extends JpaRepository<DisabelPerusahaan, Long> {

    List<DisabelPerusahaan> findByNikContaining(String nik);

    List<DisabelPerusahaan> findByNamaContainingIgnoreCase(String nama);
}
