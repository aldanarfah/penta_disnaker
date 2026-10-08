package com.disnaker.penta.controller;

import com.disnaker.penta.dto.PenempatanDalamNegeriRequestDto;
import com.disnaker.penta.dto.PenempatanDalamNegeriResponseDto;
import com.disnaker.penta.service.PenempatanDalamNegeriService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ipk/penempatan-dalam-negeri")
@RequiredArgsConstructor
@Tag(name = "IPK - Penempatan Dalam Negeri", description = "Endpoint untuk data penempatan tenaga kerja dalam negeri oleh Disnaker")
public class PenempatanDalamNegeriController {

    private final PenempatanDalamNegeriService penempatanDalamNegeriService;

    @GetMapping
    @Operation(summary = "Ambil semua data penempatan, atau cari berdasarkan nama/NIK/periode")
    public ResponseEntity<List<PenempatanDalamNegeriResponseDto>> getAll(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String nik,
            @RequestParam(required = false) Integer tahun,
            @RequestParam(required = false) Integer bulan) {

        if (nama != null && !nama.isBlank()) {
            return ResponseEntity.ok(penempatanDalamNegeriService.searchByNama(nama));
        }
        if (nik != null && !nik.isBlank()) {
            return ResponseEntity.ok(penempatanDalamNegeriService.searchByNik(nik));
        }
        if (tahun != null && bulan != null) {
            return ResponseEntity.ok(penempatanDalamNegeriService.findByPeriode(tahun, bulan));
        }
        return ResponseEntity.ok(penempatanDalamNegeriService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu data penempatan berdasarkan id")
    public ResponseEntity<PenempatanDalamNegeriResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(penempatanDalamNegeriService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah data penempatan baru")
    public ResponseEntity<PenempatanDalamNegeriResponseDto> create(
            @Valid @RequestBody PenempatanDalamNegeriRequestDto request) {
        PenempatanDalamNegeriResponseDto created = penempatanDalamNegeriService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data penempatan yang sudah ada")
    public ResponseEntity<PenempatanDalamNegeriResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PenempatanDalamNegeriRequestDto request) {
        return ResponseEntity.ok(penempatanDalamNegeriService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data penempatan")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        penempatanDalamNegeriService.delete(id);
        return ResponseEntity.noContent().build();
    }
}