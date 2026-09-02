package com.disnaker.penta.repository;

import com.disnaker.penta.entity.DisabelLansia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisabelLansiaRepository extends JpaRepository<DisabelLansia, Long> {

    List<DisabelLansia> findByNikContaining(String nik);

    List<DisabelLansia> findByNamaContainingIgnoreCase(String nama);
}
