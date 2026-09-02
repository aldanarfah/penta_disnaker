package com.disnaker.penta.repository;

import com.disnaker.penta.entity.Ak1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Ak1Repository extends JpaRepository<Ak1, Long> {

    List<Ak1> findByNikContaining(String nik);

    List<Ak1> findByNamaContainingIgnoreCase(String nama);
}
