package com.disnaker.penta.service;

import com.disnaker.penta.dto.DisabelLansiaRequestDto;
import com.disnaker.penta.dto.DisabelLansiaResponseDto;
import com.disnaker.penta.entity.DisabelLansia;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.DisabelLansiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok otomatis buat constructor untuk field final di bawah -> dependency injection
@Transactional
public class DisabelLansiaService {

    private final DisabelLansiaRepository disabelLansiaRepository;

    public List<DisabelLansiaResponseDto> findAll() {
        return disabelLansiaRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public DisabelLansiaResponseDto findById(Long id) {
        DisabelLansia entity = disabelLansiaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Disabilitas Lansia", id));
        return toResponseDto(entity);
    }

    public List<DisabelLansiaResponseDto> searchByNama(String nama) {
        return disabelLansiaRepository.findByNamaContainingIgnoreCase(nama)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<DisabelLansiaResponseDto> searchByNik(String nik) {
        return disabelLansiaRepository.findByNikContaining(nik)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public DisabelLansiaResponseDto create(DisabelLansiaRequestDto request) {
        DisabelLansia entity = toEntity(request);
        DisabelLansia saved = disabelLansiaRepository.save(entity);
        return toResponseDto(saved);
    }

    public DisabelLansiaResponseDto update(Long id, DisabelLansiaRequestDto request) {
        DisabelLansia existing = disabelLansiaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Disabilitas Lansia", id));

        existing.setNamaPerusahaan(request.getNamaPerusahaan());
        existing.setNama(request.getNama());
        existing.setNik(request.getNik());
        existing.setTempatLahir(request.getTempatLahir());
        existing.setTanggalLahir(request.getTanggalLahir());
        existing.setAlamat(request.getAlamat());
        existing.setProvinsi(request.getProvinsi());
        existing.setKabupatenKota(request.getKabupatenKota());
        existing.setNoHp(request.getNoHp());
        existing.setEmail(request.getEmail());
        existing.setKeahlian(request.getKeahlian());
        existing.setSertifikatKompetensi(request.getSertifikatKompetensi());
        existing.setPengalamanKerja(request.getPengalamanKerja());
        existing.setPendidikanTerakhir(request.getPendidikanTerakhir());
        existing.setJenisKelamin(request.getJenisKelamin());
        existing.setStatusKerja(request.getStatusKerja());
        existing.setTmtPenempatan(request.getTmtPenempatan());
        existing.setJabatan(request.getJabatan());
        existing.setStatusKepegawaian(request.getStatusKepegawaian());
        existing.setSektorUsaha(request.getSektorUsaha());
        existing.setHambatan(request.getHambatan());

        DisabelLansia updated = disabelLansiaRepository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!disabelLansiaRepository.existsById(id)) {
            throw ResourceNotFoundException.forId("Data Disabilitas Lansia", id);
        }
        disabelLansiaRepository.deleteById(id);
    }

    // ==================== Mapper Entity <-> DTO ====================

    private DisabelLansiaResponseDto toResponseDto(DisabelLansia entity) {
        return DisabelLansiaResponseDto.builder()
                .id(entity.getId())
                .namaPerusahaan(entity.getNamaPerusahaan())
                .nama(entity.getNama())
                .nik(entity.getNik())
                .tempatLahir(entity.getTempatLahir())
                .tanggalLahir(entity.getTanggalLahir())
                .alamat(entity.getAlamat())
                .provinsi(entity.getProvinsi())
                .kabupatenKota(entity.getKabupatenKota())
                .noHp(entity.getNoHp())
                .email(entity.getEmail())
                .keahlian(entity.getKeahlian())
                .sertifikatKompetensi(entity.getSertifikatKompetensi())
                .pengalamanKerja(entity.getPengalamanKerja())
                .pendidikanTerakhir(entity.getPendidikanTerakhir())
                .jenisKelamin(entity.getJenisKelamin())
                .statusKerja(entity.getStatusKerja())
                .tmtPenempatan(entity.getTmtPenempatan())
                .jabatan(entity.getJabatan())
                .statusKepegawaian(entity.getStatusKepegawaian())
                .sektorUsaha(entity.getSektorUsaha())
                .hambatan(entity.getHambatan())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private DisabelLansia toEntity(DisabelLansiaRequestDto dto) {
        return DisabelLansia.builder()
                .namaPerusahaan(dto.getNamaPerusahaan())
                .nama(dto.getNama())
                .nik(dto.getNik())
                .tempatLahir(dto.getTempatLahir())
                .tanggalLahir(dto.getTanggalLahir())
                .alamat(dto.getAlamat())
                .provinsi(dto.getProvinsi())
                .kabupatenKota(dto.getKabupatenKota())
                .noHp(dto.getNoHp())
                .email(dto.getEmail())
                .keahlian(dto.getKeahlian())
                .sertifikatKompetensi(dto.getSertifikatKompetensi())
                .pengalamanKerja(dto.getPengalamanKerja())
                .pendidikanTerakhir(dto.getPendidikanTerakhir())
                .jenisKelamin(dto.getJenisKelamin())
                .statusKerja(dto.getStatusKerja())
                .tmtPenempatan(dto.getTmtPenempatan())
                .jabatan(dto.getJabatan())
                .statusKepegawaian(dto.getStatusKepegawaian())
                .sektorUsaha(dto.getSektorUsaha())
                .hambatan(dto.getHambatan())
                .build();
    }
}