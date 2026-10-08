package com.disnaker.penta.repository;

import com.disnaker.penta.entity.PmiDokumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PmiDokumenRepository extends JpaRepository<PmiDokumen, Long> {

    List<PmiDokumen> findByPmiId(Long pmiId);
}
