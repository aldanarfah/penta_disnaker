package com.disnaker.penta.controller;

import com.disnaker.penta.dto.PemberdayaanRekapResponseDto;
import com.disnaker.penta.service.PemberdayaanRekapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disabilitas/pemberdayaan")
@RequiredArgsConstructor
@Tag(name = "Disabilitas - Pemberdayaan", description = "Endpoint untuk data kegiatan pemberdayaan penyandang disabilitas (jumlah peserta, foto dokumentasi, dan laporan)")
public class PemberdayaanRekapController {

    private final PemberdayaanRekapService pemberdayaanRekapService;

    @GetMapping("/rekap")
    @Operation(summary = "Rekap kegiatan pemberdayaan: angka ringkasan tahun terpilih, rekap per tahun, dan daftar kegiatan (tahun kosong = tahun ini)")
    public ResponseEntity<PemberdayaanRekapResponseDto> rekap(@RequestParam(required = false) Integer tahun) {
        return ResponseEntity.ok(pemberdayaanRekapService.rekap(tahun));
    }
}