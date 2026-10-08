package com.disnaker.penta.controller;

import com.disnaker.penta.dto.DisabelPerusahaanRequestDto;
import com.disnaker.penta.dto.DisabelPerusahaanResponseDto;
import com.disnaker.penta.service.DisabelPerusahaanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disabilitas/perusahaan")
@RequiredArgsConstructor
@Tag(name = "Disabilitas - Perusahaan", description = "Endpoint untuk data penyandang disabilitas yang ditempatkan/bekerja di perusahaan")
public class DisabelPerusahaanController {

    private final DisabelPerusahaanService disabelPerusahaanService;

    @GetMapping
    @Operation(summary = "Ambil semua data disabilitas perusahaan, atau cari berdasarkan nama/NIK")
    public ResponseEntity<List<DisabelPerusahaanResponseDto>> getAll(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String nik) {

        if (nama != null && !nama.isBlank()) {
            return ResponseEntity.ok(disabelPerusahaanService.searchByNama(nama));
        }
        if (nik != null && !nik.isBlank()) {
            return ResponseEntity.ok(disabelPerusahaanService.searchByNik(nik));
        }
        return ResponseEntity.ok(disabelPerusahaanService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu data disabilitas perusahaan berdasarkan id")
    public ResponseEntity<DisabelPerusahaanResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(disabelPerusahaanService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah data disabilitas perusahaan baru")
    public ResponseEntity<DisabelPerusahaanResponseDto> create(
            @Valid @RequestBody DisabelPerusahaanRequestDto request) {
        DisabelPerusahaanResponseDto created = disabelPerusahaanService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data disabilitas perusahaan yang sudah ada")
    public ResponseEntity<DisabelPerusahaanResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody DisabelPerusahaanRequestDto request) {
        return ResponseEntity.ok(disabelPerusahaanService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data disabilitas perusahaan")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        disabelPerusahaanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}