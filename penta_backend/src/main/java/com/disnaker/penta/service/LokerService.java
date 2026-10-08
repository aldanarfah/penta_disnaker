package com.disnaker.penta.service;

import com.disnaker.penta.dto.LokerRequestDto;
import com.disnaker.penta.dto.LokerResponseDto;
import com.disnaker.penta.entity.Loker;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.LokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LokerService {

    private final LokerRepository lokerRepository;

    public List<LokerResponseDto> findAll() {
        return lokerRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public LokerResponseDto findById(Long id) {
        Loker entity = lokerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Loker", id));
        return toResponseDto(entity);
    }

    public List<LokerResponseDto> searchByNamaPerusahaan(String namaPerusahaan) {
        return lokerRepository.findByNamaPerusahaanContainingIgnoreCase(namaPerusahaan)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<LokerResponseDto> searchByNamaJabatan(String namaJabatan) {
        return lokerRepository.findByNamaJabatanContainingIgnoreCase(namaJabatan)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<LokerResponseDto> findByPeriode(Integer tahun, Integer bulan) {
        return lokerRepository.findByTahunAndBulan(tahun, bulan)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public LokerResponseDto create(LokerRequestDto request) {
        Loker entity = toEntity(request);
        Loker saved = lokerRepository.save(entity);
        return toResponseDto(saved);
    }

    public LokerResponseDto update(Long id, LokerRequestDto request) {
        Loker existing = lokerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Loker", id));

        existing.setBulan(request.getBulan());
        existing.setTahun(request.getTahun());
        existing.setNamaPerusahaan(request.getNamaPerusahaan());
        existing.setNibPerusahaan(request.getNibPerusahaan());
        existing.setLapanganUsaha(request.getLapanganUsaha());
        existing.setAlamatPerusahaan(request.getAlamatPerusahaan());
        existing.setDesa(request.getDesa());
        existing.setKecamatan(request.getKecamatan());
        existing.setProvinsiPerusahaan(request.getProvinsiPerusahaan());
        existing.setKabupatenKotaPerusahaan(request.getKabupatenKotaPerusahaan());
        existing.setKodeJabatan(request.getKodeJabatan());
        existing.setNamaJabatan(request.getNamaJabatan());
        existing.setJumlahDibutuhkan(request.getJumlahDibutuhkan());
        existing.setJenisKelamin(request.getJenisKelamin());
        existing.setPendidikan(request.getPendidikan());
        existing.setKeterampilanKompetensi(request.getKeterampilanKompetensi());

        Loker updated = lokerRepository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!lokerRepository.existsById(id)) {
            throw ResourceNotFoundException.forId("Data Loker", id);
        }
        lokerRepository.deleteById(id);
    }

    private LokerResponseDto toResponseDto(Loker entity) {
        return LokerResponseDto.builder()
                .id(entity.getId())
                .bulan(entity.getBulan())
                .tahun(entity.getTahun())
                .namaPerusahaan(entity.getNamaPerusahaan())
                .nibPerusahaan(entity.getNibPerusahaan())
                .lapanganUsaha(entity.getLapanganUsaha())
                .alamatPerusahaan(entity.getAlamatPerusahaan())
                .desa(entity.getDesa())
                .kecamatan(entity.getKecamatan())
                .provinsiPerusahaan(entity.getProvinsiPerusahaan())
                .kabupatenKotaPerusahaan(entity.getKabupatenKotaPerusahaan())
                .kodeJabatan(entity.getKodeJabatan())
                .namaJabatan(entity.getNamaJabatan())
                .jumlahDibutuhkan(entity.getJumlahDibutuhkan())
                .jenisKelamin(entity.getJenisKelamin())
                .pendidikan(entity.getPendidikan())
                .keterampilanKompetensi(entity.getKeterampilanKompetensi())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private Loker toEntity(LokerRequestDto dto) {
        return Loker.builder()
                .bulan(dto.getBulan())
                .tahun(dto.getTahun())
                .namaPerusahaan(dto.getNamaPerusahaan())
                .nibPerusahaan(dto.getNibPerusahaan())
                .lapanganUsaha(dto.getLapanganUsaha())
                .alamatPerusahaan(dto.getAlamatPerusahaan())
                .desa(dto.getDesa())
                .kecamatan(dto.getKecamatan())
                .provinsiPerusahaan(dto.getProvinsiPerusahaan())
                .kabupatenKotaPerusahaan(dto.getKabupatenKotaPerusahaan())
                .kodeJabatan(dto.getKodeJabatan())
                .namaJabatan(dto.getNamaJabatan())
                .jumlahDibutuhkan(dto.getJumlahDibutuhkan())
                .jenisKelamin(dto.getJenisKelamin())
                .pendidikan(dto.getPendidikan())
                .keterampilanKompetensi(dto.getKeterampilanKompetensi())
                .build();
    }
}