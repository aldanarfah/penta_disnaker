package com.disnaker.penta.service;

import com.disnaker.penta.dto.Ak1RequestDto;
import com.disnaker.penta.dto.Ak1ResponseDto;
import com.disnaker.penta.entity.Ak1;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.Ak1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok otomatis buat constructor untuk field final di bawah -> dependency injection
@Transactional
public class Ak1Service {

    private final Ak1Repository ak1Repository;

    public List<Ak1ResponseDto> findAll() {
        return ak1Repository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public Ak1ResponseDto findById(Long id) {
        Ak1 entity = ak1Repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data AK1", id));
        return toResponseDto(entity);
    }

    public List<Ak1ResponseDto> searchByNama(String nama) {
        return ak1Repository.findByNamaContainingIgnoreCase(nama)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<Ak1ResponseDto> searchByNik(String nik) {
        return ak1Repository.findByNikContaining(nik)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public Ak1ResponseDto create(Ak1RequestDto request) {
        Ak1 entity = toEntity(request);
        Ak1 saved = ak1Repository.save(entity);
        return toResponseDto(saved);
    }

    public Ak1ResponseDto update(Long id, Ak1RequestDto request) {
        Ak1 existing = ak1Repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data AK1", id));

        existing.setNoAk1(request.getNoAk1());
        existing.setNama(request.getNama());
        existing.setNik(request.getNik());
        existing.setTanggalTerdaftar(request.getTanggalTerdaftar());
        existing.setEmail(request.getEmail());
        existing.setJenisKelamin(request.getJenisKelamin());
        existing.setPendidikanTerakhir(request.getPendidikanTerakhir());
        existing.setJurusan(request.getJurusan());
        existing.setTahunLulus(request.getTahunLulus());
        existing.setKecamatan(request.getKecamatan());
        existing.setDesaKelurahan(request.getDesaKelurahan());
        existing.setAlamat(request.getAlamat());
        existing.setNoHp(request.getNoHp());
        existing.setTempatLahir(request.getTempatLahir());
        existing.setTanggalLahir(request.getTanggalLahir());
        existing.setStatusPerkawinan(request.getStatusPerkawinan());
        existing.setTujuanMinat(request.getTujuanMinat());

        Ak1 updated = ak1Repository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!ak1Repository.existsById(id)) {
            throw ResourceNotFoundException.forId("Data AK1", id);
        }
        ak1Repository.deleteById(id);
    }

    // ==================== Mapper Entity <-> DTO ====================
    // Untuk proyek skala ini, mapping manual seperti ini sudah cukup dan mudah dibaca.
    // Kalau field makin banyak/kompleks di kemudian hari, bisa pertimbangkan library MapStruct.

    private Ak1ResponseDto toResponseDto(Ak1 entity) {
        return Ak1ResponseDto.builder()
                .id(entity.getId())
                .noAk1(entity.getNoAk1())
                .nama(entity.getNama())
                .nik(entity.getNik())
                .tanggalTerdaftar(entity.getTanggalTerdaftar())
                .email(entity.getEmail())
                .jenisKelamin(entity.getJenisKelamin())
                .pendidikanTerakhir(entity.getPendidikanTerakhir())
                .jurusan(entity.getJurusan())
                .tahunLulus(entity.getTahunLulus())
                .kecamatan(entity.getKecamatan())
                .desaKelurahan(entity.getDesaKelurahan())
                .alamat(entity.getAlamat())
                .noHp(entity.getNoHp())
                .tempatLahir(entity.getTempatLahir())
                .tanggalLahir(entity.getTanggalLahir())
                .statusPerkawinan(entity.getStatusPerkawinan())
                .tujuanMinat(entity.getTujuanMinat())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private Ak1 toEntity(Ak1RequestDto dto) {
        return Ak1.builder()
                .noAk1(dto.getNoAk1())
                .nama(dto.getNama())
                .nik(dto.getNik())
                .tanggalTerdaftar(dto.getTanggalTerdaftar())
                .email(dto.getEmail())
                .jenisKelamin(dto.getJenisKelamin())
                .pendidikanTerakhir(dto.getPendidikanTerakhir())
                .jurusan(dto.getJurusan())
                .tahunLulus(dto.getTahunLulus())
                .kecamatan(dto.getKecamatan())
                .desaKelurahan(dto.getDesaKelurahan())
                .alamat(dto.getAlamat())
                .noHp(dto.getNoHp())
                .tempatLahir(dto.getTempatLahir())
                .tanggalLahir(dto.getTanggalLahir())
                .statusPerkawinan(dto.getStatusPerkawinan())
                .tujuanMinat(dto.getTujuanMinat())
                .build();
    }
}
