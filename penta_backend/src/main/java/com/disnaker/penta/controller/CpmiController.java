package com.disnaker.penta.controller;

import com.disnaker.penta.dto.CpmiRequestDto;
import com.disnaker.penta.dto.CpmiResponseDto;
import com.disnaker.penta.service.CpmiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cpmi")
@RequiredArgsConstructor
@Tag(name = "CPMI", description = "Endpoint untuk data Calon Pekerja Migran Indonesia (rekomendasi paspor)")
public class CpmiController {

    private final CpmiService cpmiService;

    @GetMapping
    @Operation(summary = "Ambil semua data CPMI, atau cari berdasarkan nama/NIK")
    public ResponseEntity<List<CpmiResponseDto>> getAll(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String nik) {

        if (nama != null && !nama.isBlank()) {
            return ResponseEntity.ok(cpmiService.searchByNama(nama));
        }
        if (nik != null && !nik.isBlank()) {
            return ResponseEntity.ok(cpmiService.searchByNik(nik));
        }
        return ResponseEntity.ok(cpmiService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu data CPMI berdasarkan id")
    public ResponseEntity<CpmiResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cpmiService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah data CPMI baru")
    public ResponseEntity<CpmiResponseDto> create(@Valid @RequestBody CpmiRequestDto request) {
        CpmiResponseDto created = cpmiService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data CPMI yang sudah ada")
    public ResponseEntity<CpmiResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CpmiRequestDto request) {
        return ResponseEntity.ok(cpmiService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data CPMI")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cpmiService.delete(id);
        return ResponseEntity.noContent().build();
    }
}