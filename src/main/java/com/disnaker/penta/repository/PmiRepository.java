package com.disnaker.penta.repository;

import com.disnaker.penta.entity.Pmi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PmiRepository extends JpaRepository<Pmi, Long> {

    List<Pmi> findByNikContaining(String nik);

    List<Pmi> findByNamaContainingIgnoreCase(String nama);
}
