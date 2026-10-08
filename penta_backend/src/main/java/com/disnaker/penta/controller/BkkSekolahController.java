package com.disnaker.penta.controller;

import com.disnaker.penta.dto.BkkSekolahRequestDto;
import com.disnaker.penta.dto.BkkSekolahResponseDto;
import com.disnaker.penta.service.BkkSekolahService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/bkk/sekolah")
@RequiredArgsConstructor
@Tag(name = "BKK - Sekolah", description = "Endpoint untuk data SMK peserta Bursa Kerja Khusus")
public class BkkSekolahController {

    private final BkkSekolahService bkkSekolahService;

    @GetMapping
    @Operation(summary = "Ambil semua sekolah BKK, atau cari berdasarkan nama")
    public ResponseEntity<List<BkkSekolahResponseDto>> getAll(
            @RequestParam(required = false) String nama) {
        if (nama != null && !nama.isBlank()) {
            return ResponseEntity.ok(bkkSekolahService.searchByNama(nama));
        }
        return ResponseEntity.ok(bkkSekolahService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu sekolah BKK berdasarkan id")
    public ResponseEntity<BkkSekolahResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bkkSekolahService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah sekolah BKK baru (cukup nama)")
    public ResponseEntity<BkkSekolahResponseDto> create(@Valid @RequestBody BkkSekolahRequestDto request) {
        BkkSekolahResponseDto created = bkkSekolahService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui nama sekolah BKK")
    public ResponseEntity<BkkSekolahResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody BkkSekolahRequestDto request) {
        return ResponseEntity.ok(bkkSekolahService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus sekolah BKK (data alumni tetap ada, hanya kehilangan tautan sekolah)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bkkSekolahService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/upload/sk-std", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload/ganti file SK STD untuk sekolah tertentu")
    public ResponseEntity<BkkSekolahResponseDto> uploadSkStd(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(bkkSekolahService.uploadSkStd(id, file));
    }

    @PostMapping(value = "/{id}/upload/sk-pak", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload/ganti file SK PAK untuk sekolah tertentu")
    public ResponseEntity<BkkSekolahResponseDto> uploadSkPak(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(bkkSekolahService.uploadSkPak(id, file));
    }
}