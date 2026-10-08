package com.disnaker.penta.service;

import com.disnaker.penta.dto.CpmiRequestDto;
import com.disnaker.penta.dto.CpmiResponseDto;
import com.disnaker.penta.entity.Cpmi;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.CpmiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok otomatis buat constructor untuk field final di bawah -> dependency injection
@Transactional
public class CpmiService {

    private final CpmiRepository cpmiRepository;

    public List<CpmiResponseDto> findAll() {
        return cpmiRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public CpmiResponseDto findById(Long id) {
        Cpmi entity = cpmiRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data CPMI", id));
        return toResponseDto(entity);
    }

    public List<CpmiResponseDto> searchByNama(String nama) {
        return cpmiRepository.findByNamaContainingIgnoreCase(nama)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<CpmiResponseDto> searchByNik(String nik) {
        return cpmiRepository.findByNikContaining(nik)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public CpmiResponseDto create(CpmiRequestDto request) {
        Cpmi entity = toEntity(request);
        Cpmi saved = cpmiRepository.save(entity);
        return toResponseDto(saved);
    }

    public CpmiResponseDto update(Long id, CpmiRequestDto request) {
        Cpmi existing = cpmiRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data CPMI", id));

        existing.setTanggalRekom(request.getTanggalRekom());
        existing.setNama(request.getNama());
        existing.setTempatLahir(request.getTempatLahir());
        existing.setTanggalLahir(request.getTanggalLahir());
        existing.setAlamat(request.getAlamat());
        existing.setDesaKelurahan(request.getDesaKelurahan());
        existing.setKecamatan(request.getKecamatan());
        existing.setJenisKelamin(request.getJenisKelamin());
        existing.setPendidikanTerakhir(request.getPendidikanTerakhir());
        existing.setJabatan(request.getJabatan());
        existing.setNegaraTujuan(request.getNegaraTujuan());
        existing.setPerusahaanPengirim(request.getPerusahaanPengirim());
        existing.setPemberiKerja(request.getPemberiKerja());
        existing.setNoHp(request.getNoHp());
        existing.setNik(request.getNik());
        existing.setNoSertifikatKompetensi(request.getNoSertifikatKompetensi());
        existing.setNoReg(request.getNoReg());
        existing.setBidang(request.getBidang());
        existing.setKualifikasiKompetensi(request.getKualifikasiKompetensi());
        existing.setKeterangan(request.getKeterangan());

        Cpmi updated = cpmiRepository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!cpmiRepository.existsById(id)) {
            throw ResourceNotFoundException.forId("Data CPMI", id);
        }
        cpmiRepository.deleteById(id);
    }

    // ==================== Mapper Entity <-> DTO ====================

    private CpmiResponseDto toResponseDto(Cpmi entity) {
        return CpmiResponseDto.builder()
                .id(entity.getId())
                .tanggalRekom(entity.getTanggalRekom())
                .nama(entity.getNama())
                .tempatLahir(entity.getTempatLahir())
                .tanggalLahir(entity.getTanggalLahir())
                .alamat(entity.getAlamat())
                .desaKelurahan(entity.getDesaKelurahan())
                .kecamatan(entity.getKecamatan())
                .jenisKelamin(entity.getJenisKelamin())
                .pendidikanTerakhir(entity.getPendidikanTerakhir())
                .jabatan(entity.getJabatan())
                .negaraTujuan(entity.getNegaraTujuan())
                .perusahaanPengirim(entity.getPerusahaanPengirim())
                .pemberiKerja(entity.getPemberiKerja())
                .noHp(entity.getNoHp())
                .nik(entity.getNik())
                .noSertifikatKompetensi(entity.getNoSertifikatKompetensi())
                .noReg(entity.getNoReg())
                .bidang(entity.getBidang())
                .kualifikasiKompetensi(entity.getKualifikasiKompetensi())
                .keterangan(entity.getKeterangan())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private Cpmi toEntity(CpmiRequestDto dto) {
        return Cpmi.builder()
                .tanggalRekom(dto.getTanggalRekom())
                .nama(dto.getNama())
                .tempatLahir(dto.getTempatLahir())
                .tanggalLahir(dto.getTanggalLahir())
                .alamat(dto.getAlamat())
                .desaKelurahan(dto.getDesaKelurahan())
                .kecamatan(dto.getKecamatan())
                .jenisKelamin(dto.getJenisKelamin())
                .pendidikanTerakhir(dto.getPendidikanTerakhir())
                .jabatan(dto.getJabatan())
                .negaraTujuan(dto.getNegaraTujuan())
                .perusahaanPengirim(dto.getPerusahaanPengirim())
                .pemberiKerja(dto.getPemberiKerja())
                .noHp(dto.getNoHp())
                .nik(dto.getNik())
                .noSertifikatKompetensi(dto.getNoSertifikatKompetensi())
                .noReg(dto.getNoReg())
                .bidang(dto.getBidang())
                .kualifikasiKompetensi(dto.getKualifikasiKompetensi())
                .keterangan(dto.getKeterangan())
                .build();
    }
}