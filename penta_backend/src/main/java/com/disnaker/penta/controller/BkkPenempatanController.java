package com.disnaker.penta.controller;

import com.disnaker.penta.dto.BkkPenempatanRequestDto;
import com.disnaker.penta.dto.BkkPenempatanResponseDto;
import com.disnaker.penta.service.BkkPenempatanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bkk/penempatan")
@RequiredArgsConstructor
@Tag(name = "BKK - Penempatan", description = "Endpoint untuk data penempatan tenaga kerja alumni SMK")
public class BkkPenempatanController {

    private final BkkPenempatanService bkkPenempatanService;

    @GetMapping
    @Operation(summary = "Ambil semua data penempatan, atau cari berdasarkan nama/NIK/sekolah")
    public ResponseEntity<List<BkkPenempatanResponseDto>> getAll(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String nik,
            @RequestParam(required = false) Long sekolahId) {

        if (nama != null && !nama.isBlank()) {
            return ResponseEntity.ok(bkkPenempatanService.searchByNama(nama));
        }
        if (nik != null && !nik.isBlank()) {
            return ResponseEntity.ok(bkkPenempatanService.searchByNik(nik));
        }
        if (sekolahId != null) {
            return ResponseEntity.ok(bkkPenempatanService.findBySekolah(sekolahId));
        }
        return ResponseEntity.ok(bkkPenempatanService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu data penempatan berdasarkan id")
    public ResponseEntity<BkkPenempatanResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bkkPenempatanService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah data penempatan baru")
    public ResponseEntity<BkkPenempatanResponseDto> create(
            @Valid @RequestBody BkkPenempatanRequestDto request) {
        BkkPenempatanResponseDto created = bkkPenempatanService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data penempatan yang sudah ada")
    public ResponseEntity<BkkPenempatanResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody BkkPenempatanRequestDto request) {
        return ResponseEntity.ok(bkkPenempatanService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data penempatan")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bkkPenempatanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}