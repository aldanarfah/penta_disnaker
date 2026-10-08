package com.disnaker.penta.controller;

import com.disnaker.penta.dto.DisabelLansiaRequestDto;
import com.disnaker.penta.dto.DisabelLansiaResponseDto;
import com.disnaker.penta.service.DisabelLansiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disabilitas/lansia")
@RequiredArgsConstructor
@Tag(name = "Disabilitas - Lansia", description = "Endpoint untuk data lansia yang ditempatkan/bekerja/diberdayakan")
public class DisabelLansiaController {

    private final DisabelLansiaService disabelLansiaService;

    @GetMapping
    @Operation(summary = "Ambil semua data disabilitas lansia, atau cari berdasarkan nama/NIK")
    public ResponseEntity<List<DisabelLansiaResponseDto>> getAll(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String nik) {

        if (nama != null && !nama.isBlank()) {
            return ResponseEntity.ok(disabelLansiaService.searchByNama(nama));
        }
        if (nik != null && !nik.isBlank()) {
            return ResponseEntity.ok(disabelLansiaService.searchByNik(nik));
        }
        return ResponseEntity.ok(disabelLansiaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu data disabilitas lansia berdasarkan id")
    public ResponseEntity<DisabelLansiaResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(disabelLansiaService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah data disabilitas lansia baru")
    public ResponseEntity<DisabelLansiaResponseDto> create(
            @Valid @RequestBody DisabelLansiaRequestDto request) {
        DisabelLansiaResponseDto created = disabelLansiaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data disabilitas lansia yang sudah ada")
    public ResponseEntity<DisabelLansiaResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody DisabelLansiaRequestDto request) {
        return ResponseEntity.ok(disabelLansiaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data disabilitas lansia")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        disabelLansiaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}