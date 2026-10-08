package com.disnaker.penta.service;

import com.disnaker.penta.dto.BkkSekolahRequestDto;
import com.disnaker.penta.dto.BkkSekolahResponseDto;
import com.disnaker.penta.entity.BkkSekolah;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.BkkSekolahRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BkkSekolahService {

    private static final String SUB_FOLDER = "bkk-sekolah";

    private final BkkSekolahRepository bkkSekolahRepository;
    private final FileStorageService fileStorageService;

    public List<BkkSekolahResponseDto> findAll() {
        return bkkSekolahRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public BkkSekolahResponseDto findById(Long id) {
        BkkSekolah entity = bkkSekolahRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Sekolah BKK", id));
        return toResponseDto(entity);
    }

    public List<BkkSekolahResponseDto> searchByNama(String namaSekolah) {
        return bkkSekolahRepository.findByNamaSekolahContainingIgnoreCase(namaSekolah)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public BkkSekolahResponseDto create(BkkSekolahRequestDto request) {
        BkkSekolah entity = BkkSekolah.builder()
                .namaSekolah(request.getNamaSekolah())
                .build();
        BkkSekolah saved = bkkSekolahRepository.save(entity);
        return toResponseDto(saved);
    }

    public BkkSekolahResponseDto update(Long id, BkkSekolahRequestDto request) {
        BkkSekolah existing = bkkSekolahRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Sekolah BKK", id));
        existing.setNamaSekolah(request.getNamaSekolah());
        BkkSekolah updated = bkkSekolahRepository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        BkkSekolah existing = bkkSekolahRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Sekolah BKK", id));

        // Hapus file fisik SK STD/SK PAK kalau ada, supaya tidak jadi file sampah di server
        if (existing.getSkStd() != null) {
            fileStorageService.delete(existing.getSkStd());
        }
        if (existing.getSkPak() != null) {
            fileStorageService.delete(existing.getSkPak());
        }

        // Data bkk_penempatan yang terhubung TIDAK ikut terhapus.
        // sekolah_id di data tersebut otomatis menjadi NULL (ON DELETE SET NULL di database).
        bkkSekolahRepository.deleteById(id);
    }

    public BkkSekolahResponseDto uploadSkStd(Long id, MultipartFile file) {
        BkkSekolah existing = bkkSekolahRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Sekolah BKK", id));

        // Kalau sebelumnya sudah ada file, hapus dulu file lama sebelum menyimpan yang baru
        if (existing.getSkStd() != null) {
            fileStorageService.delete(existing.getSkStd());
        }

        String path = fileStorageService.store(file, SUB_FOLDER);
        existing.setSkStd(path);

        BkkSekolah updated = bkkSekolahRepository.save(existing);
        return toResponseDto(updated);
    }

    public BkkSekolahResponseDto uploadSkPak(Long id, MultipartFile file) {
        BkkSekolah existing = bkkSekolahRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Sekolah BKK", id));

        if (existing.getSkPak() != null) {
            fileStorageService.delete(existing.getSkPak());
        }

        String path = fileStorageService.store(file, SUB_FOLDER);
        existing.setSkPak(path);

        BkkSekolah updated = bkkSekolahRepository.save(existing);
        return toResponseDto(updated);
    }

    private BkkSekolahResponseDto toResponseDto(BkkSekolah entity) {
        return BkkSekolahResponseDto.builder()
                .id(entity.getId())
                .namaSekolah(entity.getNamaSekolah())
                .skStd(entity.getSkStd())
                .skPak(entity.getSkPak())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}