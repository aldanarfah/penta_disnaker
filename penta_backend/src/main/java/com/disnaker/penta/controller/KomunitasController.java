package com.disnaker.penta.controller;

import com.disnaker.penta.dto.KomunitasFotoResponseDto;
import com.disnaker.penta.dto.KomunitasRequestDto;
import com.disnaker.penta.dto.KomunitasResponseDto;
import com.disnaker.penta.entity.KomunitasFoto;
import com.disnaker.penta.entity.KomunitasKegiatan;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.service.FileStorageService;
import com.disnaker.penta.service.KomunitasFotoService;
import com.disnaker.penta.service.KomunitasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/disabilitas/komunitas")
@RequiredArgsConstructor
@Tag(name = "Disabilitas - Komunitas", description = "Endpoint untuk data kegiatan komunitas disabilitas (foto dokumentasi dan laporan)")
public class KomunitasController {

    private final KomunitasService komunitasService;
    private final KomunitasFotoService komunitasFotoService;
    private final FileStorageService fileStorageService;

    @GetMapping
    @Operation(summary = "Ambil semua kegiatan (terbaru di atas), bisa difilter tahun dan dicari berdasarkan nama kegiatan/tempat")
    public ResponseEntity<List<KomunitasResponseDto>> getAll(
            @RequestParam(required = false) Integer tahun,
            @RequestParam(required = false) String cari) {
        return ResponseEntity.ok(komunitasService.findAll(tahun, cari));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ambil satu kegiatan berdasarkan id")
    public ResponseEntity<KomunitasResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(komunitasService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tambah kegiatan baru (foto dan laporan diunggah terpisah setelahnya)")
    public ResponseEntity<KomunitasResponseDto> create(@Valid @RequestBody KomunitasRequestDto request) {
        KomunitasResponseDto created = komunitasService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Perbarui data kegiatan (foto dan laporan tidak ikut berubah)")
    public ResponseEntity<KomunitasResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody KomunitasRequestDto request) {
        return ResponseEntity.ok(komunitasService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus kegiatan beserta semua foto dan laporannya")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        komunitasService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Foto dokumentasi ====================

    @PostMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload 1-2 foto dokumentasi (JPG/PNG/WEBP, maks 5 MB per foto, total maksimal 2 foto per kegiatan)")
    public ResponseEntity<List<KomunitasFotoResponseDto>> uploadFoto(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files) {
        List<KomunitasFotoResponseDto> result = komunitasFotoService.upload(id, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/foto/{fotoId}")
    @Operation(summary = "Ambil gambar foto (wajib login). Frontend mengambilnya dengan fetch + header Authorization")
    public ResponseEntity<Resource> lihatFoto(@PathVariable Long fotoId) {
        KomunitasFoto foto = komunitasFotoService.findEntityById(fotoId);
        Resource resource = bacaFile(foto.getPathFile());

        return ResponseEntity.ok()
                .contentType(mediaTypeFoto(foto.getPathFile()))
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePrivate())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(foto.getNamaFile(), StandardCharsets.UTF_8).build().toString())
                .body(resource);
    }

    @DeleteMapping("/foto/{fotoId}")
    @Operation(summary = "Hapus satu foto (file fisik + record database)")
    public ResponseEntity<Void> hapusFoto(@PathVariable Long fotoId) {
        komunitasFotoService.delete(fotoId);
        return ResponseEntity.noContent().build();
    }

    // ==================== Laporan ====================

    @PostMapping(value = "/{id}/laporan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload atau ganti file laporan (PDF/Word, maks 10 MB)")
    public ResponseEntity<KomunitasResponseDto> uploadLaporan(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(komunitasService.uploadLaporan(id, file));
    }

    @DeleteMapping("/{id}/laporan")
    @Operation(summary = "Hapus file laporan")
    public ResponseEntity<KomunitasResponseDto> hapusLaporan(@PathVariable Long id) {
        return ResponseEntity.ok(komunitasService.hapusLaporan(id));
    }

    @GetMapping("/{id}/laporan/download")
    @Operation(summary = "Download file laporan")
    public ResponseEntity<Resource> unduhLaporan(@PathVariable Long id) {
        KomunitasKegiatan kegiatan = komunitasService.findEntityById(id);
        if (kegiatan.getLaporanPath() == null) {
            throw new ResourceNotFoundException("Kegiatan ini belum punya file laporan");
        }
        Resource resource = bacaFile(kegiatan.getLaporanPath());

        return ResponseEntity.ok()
                .contentType(mediaTypeLaporan(kegiatan.getLaporanPath()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(kegiatan.getLaporanNama(), StandardCharsets.UTF_8).build().toString())
                .body(resource);
    }

    // ==================== Helper ====================

    private Resource bacaFile(String pathRelatif) {
        Resource resource = new FileSystemResource(fileStorageService.resolve(pathRelatif));
        if (!resource.exists() || !resource.isReadable()) {
            throw new ResourceNotFoundException("File tidak ditemukan di server");
        }
        return resource;
    }

    private MediaType mediaTypeFoto(String path) {
        String p = path.toLowerCase(Locale.ROOT);
        if (p.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (p.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        }
        return MediaType.IMAGE_JPEG;
    }

    private MediaType mediaTypeLaporan(String path) {
        String p = path.toLowerCase(Locale.ROOT);
        if (p.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF;
        }
        if (p.endsWith(".docx")) {
            return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        }
        if (p.endsWith(".doc")) {
            return MediaType.parseMediaType("application/msword");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}