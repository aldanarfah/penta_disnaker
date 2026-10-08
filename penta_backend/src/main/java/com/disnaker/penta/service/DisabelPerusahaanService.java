package com.disnaker.penta.service;

import com.disnaker.penta.dto.DisabelPerusahaanRequestDto;
import com.disnaker.penta.dto.DisabelPerusahaanResponseDto;
import com.disnaker.penta.entity.DisabelPerusahaan;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.DisabelPerusahaanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok otomatis buat constructor untuk field final di bawah -> dependency injection
@Transactional
public class DisabelPerusahaanService {

    private final DisabelPerusahaanRepository disabelPerusahaanRepository;

    public List<DisabelPerusahaanResponseDto> findAll() {
        return disabelPerusahaanRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public DisabelPerusahaanResponseDto findById(Long id) {
        DisabelPerusahaan entity = disabelPerusahaanRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Disabilitas Perusahaan", id));
        return toResponseDto(entity);
    }

    public List<DisabelPerusahaanResponseDto> searchByNama(String nama) {
        return disabelPerusahaanRepository.findByNamaContainingIgnoreCase(nama)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<DisabelPerusahaanResponseDto> searchByNik(String nik) {
        return disabelPerusahaanRepository.findByNikContaining(nik)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public DisabelPerusahaanResponseDto create(DisabelPerusahaanRequestDto request) {
        DisabelPerusahaan entity = toEntity(request);
        DisabelPerusahaan saved = disabelPerusahaanRepository.save(entity);
        return toResponseDto(saved);
    }

    public DisabelPerusahaanResponseDto update(Long id, DisabelPerusahaanRequestDto request) {
        DisabelPerusahaan existing = disabelPerusahaanRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Disabilitas Perusahaan", id));

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
        existing.setRagamDisabilitas(request.getRagamDisabilitas());
        existing.setSpesifikRagam(request.getSpesifikRagam());
        existing.setStatusKerja(request.getStatusKerja());
        existing.setTmtPenempatan(request.getTmtPenempatan());
        existing.setJabatan(request.getJabatan());
        existing.setStatusKepegawaian(request.getStatusKepegawaian());
        existing.setSektorUsaha(request.getSektorUsaha());
        existing.setHambatan(request.getHambatan());

        DisabelPerusahaan updated = disabelPerusahaanRepository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!disabelPerusahaanRepository.existsById(id)) {
            throw ResourceNotFoundException.forId("Data Disabilitas Perusahaan", id);
        }
        disabelPerusahaanRepository.deleteById(id);
    }

    // ==================== Mapper Entity <-> DTO ====================

    private DisabelPerusahaanResponseDto toResponseDto(DisabelPerusahaan entity) {
        return DisabelPerusahaanResponseDto.builder()
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
                .ragamDisabilitas(entity.getRagamDisabilitas())
                .spesifikRagam(entity.getSpesifikRagam())
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

    private DisabelPerusahaan toEntity(DisabelPerusahaanRequestDto dto) {
        return DisabelPerusahaan.builder()
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
                .ragamDisabilitas(dto.getRagamDisabilitas())
                .spesifikRagam(dto.getSpesifikRagam())
                .statusKerja(dto.getStatusKerja())
                .tmtPenempatan(dto.getTmtPenempatan())
                .jabatan(dto.getJabatan())
                .statusKepegawaian(dto.getStatusKepegawaian())
                .sektorUsaha(dto.getSektorUsaha())
                .hambatan(dto.getHambatan())
                .build();
    }
}