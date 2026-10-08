package com.disnaker.penta.controller;

import com.disnaker.penta.dto.Ak1RequestDto;
import com.disnaker.penta.dto.Ak1ResponseDto;
import com.disnaker.penta.service.Ak1Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ak1")
@RequiredArgsConstructor
@Tag(name = "AK1", description = "Endpoint untuk data pencari kerja dalam negeri (Kartu Kuning)")
public class Ak1Controller {

    private final Ak1Service ak1Service;

    @GetMapping
    @Operation(summary = "Ambil semua data AK1, atau cari berdasarkan nama/NIK")
    public ResponseEntity<List<Ak1ResponseDto>> getAll(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String nik) {

        if (nama != null && !nama.isBlank()) {
            return ResponseEntity.ok(ak1Service.searchByNama(nama));
        }
        if (nik != null && !nik.isBlank()) {
            return ResponseEntity.ok(ak1Service.searchByNik(nik));
        }
        return ResponseEntity.ok(ak1Service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu data AK1 berdasarkan id")
    public ResponseEntity<Ak1ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ak1Service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah data AK1 baru")
    public ResponseEntity<Ak1ResponseDto> create(@Valid @RequestBody Ak1RequestDto request) {
        Ak1ResponseDto created = ak1Service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data AK1 yang sudah ada")
    public ResponseEntity<Ak1ResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody Ak1RequestDto request) {
        return ResponseEntity.ok(ak1Service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus data AK1")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ak1Service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
