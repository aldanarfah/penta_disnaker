package com.disnaker.penta.controller;

import com.disnaker.penta.dto.KomunitasRekapResponseDto;
import com.disnaker.penta.service.KomunitasRekapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disabilitas/komunitas")
@RequiredArgsConstructor
@Tag(name = "Disabilitas - Komunitas", description = "Endpoint untuk data kegiatan komunitas disabilitas (foto dokumentasi dan laporan)")
public class KomunitasRekapController {

    private final KomunitasRekapService komunitasRekapService;

    @GetMapping("/rekap")
    @Operation(summary = "Rekap kegiatan komunitas per tahun: capaian, status 12 bulan, total foto dan laporan (tahun kosong = tahun ini)")
    public ResponseEntity<KomunitasRekapResponseDto> rekap(@RequestParam(required = false) Integer tahun) {
        return ResponseEntity.ok(komunitasRekapService.rekap(tahun));
    }
}