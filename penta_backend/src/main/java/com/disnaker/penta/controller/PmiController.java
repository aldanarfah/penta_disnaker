package com.disnaker.penta.controller;

import com.disnaker.penta.dto.PmiRequestDto;
import com.disnaker.penta.dto.PmiResponseDto;
import com.disnaker.penta.service.PmiService;
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
@RequestMapping("/api/pmi")
@RequiredArgsConstructor
@Tag(name = "PMI", description = "Endpoint untuk data PMI (Pekerja Migran Indonesia) Bermasalah")
public class PmiController {

    private final PmiService pmiService;

    @GetMapping
    @Operation(summary = "Ambil semua data PMI, atau cari berdasarkan nama/NIK/no. paspor")
    public ResponseEntity<List<PmiResponseDto>> getAll(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String nik,
            @RequestParam(required = false) String noPaspor) {

        if (nama != null && !nama.isBlank()) {
            return ResponseEntity.ok(pmiService.searchByNama(nama));
        }
        if (nik != null && !nik.isBlank()) {
            return ResponseEntity.ok(pmiService.searchByNik(nik));
        }
        if (noPaspor != null && !noPaspor.isBlank()) {
            return ResponseEntity.ok(pmiService.searchByNoPaspor(noPaspor));
        }
        return ResponseEntity.ok(pmiService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu data PMI berdasarkan id")
    public ResponseEntity<PmiResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pmiService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah data PMI baru")
    public ResponseEntity<PmiResponseDto> create(@Valid @RequestBody PmiRequestDto request) {
        PmiResponseDto created = pmiService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data PMI yang sudah ada")
    public ResponseEntity<PmiResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody PmiRequestDto request) {
        return ResponseEntity.ok(pmiService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data PMI")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pmiService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/upload-foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload/ganti foto untuk data PMI tertentu")
    public ResponseEntity<PmiResponseDto> uploadFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(pmiService.uploadFoto(id, file));
    }
}