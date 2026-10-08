package com.disnaker.penta.service;

import com.disnaker.penta.dto.PmiRequestDto;
import com.disnaker.penta.dto.PmiResponseDto;
import com.disnaker.penta.entity.Pmi;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.PmiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PmiService {

    private static final String SUB_FOLDER = "pmi-foto";

    private final PmiRepository pmiRepository;
    private final FileStorageService fileStorageService;

    public List<PmiResponseDto> findAll() {
        return pmiRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public PmiResponseDto findById(Long id) {
        Pmi entity = pmiRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data PMI", id));
        return toResponseDto(entity);
    }

    public List<PmiResponseDto> searchByNama(String nama) {
        return pmiRepository.findByNamaContainingIgnoreCase(nama)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<PmiResponseDto> searchByNik(String nik) {
        return pmiRepository.findByNikContaining(nik)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<PmiResponseDto> searchByNoPaspor(String noPaspor) {
        return pmiRepository.findByNoPasporContaining(noPaspor)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public PmiResponseDto create(PmiRequestDto request) {
        Pmi entity = toEntity(request);
        Pmi saved = pmiRepository.save(entity);
        return toResponseDto(saved);
    }

    public PmiResponseDto update(Long id, PmiRequestDto request) {
        Pmi existing = pmiRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data PMI", id));

        existing.setNama(request.getNama());
        existing.setNik(request.getNik());
        existing.setNoPaspor(request.getNoPaspor());
        existing.setStatusPmi(request.getStatusPmi());
        existing.setTempatLahir(request.getTempatLahir());
        existing.setTanggalLahir(request.getTanggalLahir());
        existing.setAlamat(request.getAlamat());
        existing.setPermasalahan(request.getPermasalahan());
        existing.setNegara(request.getNegara());
        existing.setJenisKelamin(request.getJenisKelamin());
        existing.setTanggalPemulangan(request.getTanggalPemulangan());
        existing.setKeterangan(request.getKeterangan());
        // "foto" TIDAK disentuh di sini - hanya lewat uploadFoto()

        Pmi updated = pmiRepository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        Pmi existing = pmiRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data PMI", id));

        // Hapus file foto fisik kalau ada, supaya tidak jadi file sampah di server
        if (existing.getFoto() != null) {
            fileStorageService.delete(existing.getFoto());
        }

        pmiRepository.deleteById(id);
    }

    public PmiResponseDto uploadFoto(Long id, MultipartFile file) {
        Pmi existing = pmiRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data PMI", id));

        // Kalau sebelumnya sudah ada foto, hapus dulu file lama sebelum menyimpan yang baru
        if (existing.getFoto() != null) {
            fileStorageService.delete(existing.getFoto());
        }

        String path = fileStorageService.store(file, SUB_FOLDER);
        existing.setFoto(path);

        Pmi updated = pmiRepository.save(existing);
        return toResponseDto(updated);
    }

    private PmiResponseDto toResponseDto(Pmi entity) {
        return PmiResponseDto.builder()
                .id(entity.getId())
                .nama(entity.getNama())
                .nik(entity.getNik())
                .noPaspor(entity.getNoPaspor())
                .statusPmi(entity.getStatusPmi())
                .tempatLahir(entity.getTempatLahir())
                .tanggalLahir(entity.getTanggalLahir())
                .alamat(entity.getAlamat())
                .permasalahan(entity.getPermasalahan())
                .negara(entity.getNegara())
                .jenisKelamin(entity.getJenisKelamin())
                .tanggalPemulangan(entity.getTanggalPemulangan())
                .keterangan(entity.getKeterangan())
                .foto(entity.getFoto())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private Pmi toEntity(PmiRequestDto dto) {
        return Pmi.builder()
                .nama(dto.getNama())
                .nik(dto.getNik())
                .noPaspor(dto.getNoPaspor())
                .statusPmi(dto.getStatusPmi())
                .tempatLahir(dto.getTempatLahir())
                .tanggalLahir(dto.getTanggalLahir())
                .alamat(dto.getAlamat())
                .permasalahan(dto.getPermasalahan())
                .negara(dto.getNegara())
                .jenisKelamin(dto.getJenisKelamin())
                .tanggalPemulangan(dto.getTanggalPemulangan())
                .keterangan(dto.getKeterangan())
                // "foto" tidak diisi di sini - selalu null saat data baru dibuat
                .build();
    }
}