package com.disnaker.penta.controller;

import com.disnaker.penta.dto.PmiDokumenResponseDto;
import com.disnaker.penta.entity.PmiDokumen;
import com.disnaker.penta.service.FileStorageService;
import com.disnaker.penta.service.PmiDokumenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/pmi")
@RequiredArgsConstructor
@Tag(name = "PMI Dokumen", description = "Upload, lihat, dan hapus dokumen pendukung PMI Bermasalah (paspor, kontrak kerja, dll)")
public class PmiDokumenController {

    private final PmiDokumenService pmiDokumenService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{pmiId}/dokumen")
    @Operation(summary = "Ambil daftar dokumen milik satu data PMI")
    public ResponseEntity<List<PmiDokumenResponseDto>> getByPmiId(@PathVariable Long pmiId) {
        return ResponseEntity.ok(pmiDokumenService.findByPmiId(pmiId));
    }

    @PostMapping(value = "/{pmiId}/dokumen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload dokumen baru untuk satu data PMI (paspor, kontrak kerja, dll)")
    public ResponseEntity<PmiDokumenResponseDto> upload(
            @PathVariable Long pmiId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "jenisDokumen", required = false) String jenisDokumen) {
        PmiDokumenResponseDto result = pmiDokumenService.upload(pmiId, file, jenisDokumen);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/dokumen/{dokumenId}/download")
    @Operation(summary = "Download/lihat file dokumen berdasarkan id dokumen")
    public ResponseEntity<Resource> download(@PathVariable Long dokumenId) {
        PmiDokumen dokumen = pmiDokumenService.findEntityById(dokumenId);
        Path filePath = fileStorageService.resolve(dokumen.getPathFile());

        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Lokasi file tidak valid", e);
        }

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + dokumen.getNamaFile() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/dokumen/{dokumenId}")
    @Operation(summary = "Hapus dokumen (file fisik + record database)")
    public ResponseEntity<Void> delete(@PathVariable Long dokumenId) {
        pmiDokumenService.delete(dokumenId);
        return ResponseEntity.noContent().build();
    }
}
