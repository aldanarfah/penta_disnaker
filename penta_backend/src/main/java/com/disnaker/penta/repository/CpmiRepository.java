package com.disnaker.penta.repository;

import com.disnaker.penta.entity.Cpmi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpmiRepository extends JpaRepository<Cpmi, Long> {

    List<Cpmi> findByNikContaining(String nik);

    List<Cpmi> findByNamaContainingIgnoreCase(String nama);

    List<Cpmi> findByNegaraTujuanContainingIgnoreCase(String negaraTujuan);
}
