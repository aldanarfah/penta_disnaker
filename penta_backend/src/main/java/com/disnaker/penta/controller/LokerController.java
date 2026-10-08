package com.disnaker.penta.controller;

import com.disnaker.penta.dto.LokerRequestDto;
import com.disnaker.penta.dto.LokerResponseDto;
import com.disnaker.penta.service.LokerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ipk/loker")
@RequiredArgsConstructor
@Tag(name = "IPK - Loker", description = "Endpoint untuk data Lowongan Kerja Terdaftar")
public class LokerController {

    private final LokerService lokerService;

    @GetMapping
    @Operation(summary = "Ambil semua data loker, atau cari berdasarkan nama perusahaan/jabatan/periode")
    public ResponseEntity<List<LokerResponseDto>> getAll(
            @RequestParam(required = false) String namaPerusahaan,
            @RequestParam(required = false) String namaJabatan,
            @RequestParam(required = false) Integer tahun,
            @RequestParam(required = false) Integer bulan) {

        if (namaPerusahaan != null && !namaPerusahaan.isBlank()) {
            return ResponseEntity.ok(lokerService.searchByNamaPerusahaan(namaPerusahaan));
        }
        if (namaJabatan != null && !namaJabatan.isBlank()) {
            return ResponseEntity.ok(lokerService.searchByNamaJabatan(namaJabatan));
        }
        if (tahun != null && bulan != null) {
            return ResponseEntity.ok(lokerService.findByPeriode(tahun, bulan));
        }
        return ResponseEntity.ok(lokerService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu data loker berdasarkan id")
    public ResponseEntity<LokerResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(lokerService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah data loker baru")
    public ResponseEntity<LokerResponseDto> create(@Valid @RequestBody LokerRequestDto request) {
        LokerResponseDto created = lokerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data loker yang sudah ada")
    public ResponseEntity<LokerResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody LokerRequestDto request) {
        return ResponseEntity.ok(lokerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data loker")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lokerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}