package com.disnaker.penta.service;

import com.disnaker.penta.dto.PenempatanDalamNegeriRequestDto;
import com.disnaker.penta.dto.PenempatanDalamNegeriResponseDto;
import com.disnaker.penta.entity.PenempatanDalamNegeri;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.PenempatanDalamNegeriRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PenempatanDalamNegeriService {

    private final PenempatanDalamNegeriRepository penempatanDalamNegeriRepository;

    public List<PenempatanDalamNegeriResponseDto> findAll() {
        return penempatanDalamNegeriRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public PenempatanDalamNegeriResponseDto findById(Long id) {
        PenempatanDalamNegeri entity = penempatanDalamNegeriRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Penempatan Dalam Negeri", id));
        return toResponseDto(entity);
    }

    public List<PenempatanDalamNegeriResponseDto> searchByNama(String nama) {
        return penempatanDalamNegeriRepository.findByNamaContainingIgnoreCase(nama)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<PenempatanDalamNegeriResponseDto> searchByNik(String nik) {
        return penempatanDalamNegeriRepository.findByNikContaining(nik)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<PenempatanDalamNegeriResponseDto> findByPeriode(Integer tahun, Integer bulan) {
        return penempatanDalamNegeriRepository.findByTahunAndBulan(tahun, bulan)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public PenempatanDalamNegeriResponseDto create(PenempatanDalamNegeriRequestDto request) {
        PenempatanDalamNegeri entity = toEntity(request);
        PenempatanDalamNegeri saved = penempatanDalamNegeriRepository.save(entity);
        return toResponseDto(saved);
    }

    public PenempatanDalamNegeriResponseDto update(Long id, PenempatanDalamNegeriRequestDto request) {
        PenempatanDalamNegeri existing = penempatanDalamNegeriRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Penempatan Dalam Negeri", id));

        existing.setBulan(request.getBulan());
        existing.setTahun(request.getTahun());
        existing.setNama(request.getNama());
        existing.setNik(request.getNik());
        existing.setAlamat(request.getAlamat());
        existing.setDesa(request.getDesa());
        existing.setKecamatan(request.getKecamatan());
        existing.setProvinsi(request.getProvinsi());
        existing.setKabupatenKota(request.getKabupatenKota());
        existing.setEmail(request.getEmail());
        existing.setNoHp(request.getNoHp());
        existing.setJenisKelamin(request.getJenisKelamin());
        existing.setPendidikan(request.getPendidikan());
        existing.setProvinsiPenempatan(request.getProvinsiPenempatan());
        existing.setKabupatenKotaPenempatan(request.getKabupatenKotaPenempatan());
        existing.setNamaPerusahaan(request.getNamaPerusahaan());
        existing.setNibPerusahaan(request.getNibPerusahaan());
        existing.setAlamatPerusahaan(request.getAlamatPerusahaan());
        existing.setProvinsiPerusahaan(request.getProvinsiPerusahaan());
        existing.setKabupatenKotaPerusahaan(request.getKabupatenKotaPerusahaan());
        existing.setNamaJabatan(request.getNamaJabatan());
        existing.setLapanganUsaha(request.getLapanganUsaha());
        existing.setTanggalMulai(request.getTanggalMulai());
        existing.setGajiUpah(request.getGajiUpah());

        PenempatanDalamNegeri updated = penempatanDalamNegeriRepository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!penempatanDalamNegeriRepository.existsById(id)) {
            throw ResourceNotFoundException.forId("Data Penempatan Dalam Negeri", id);
        }
        penempatanDalamNegeriRepository.deleteById(id);
    }

    private PenempatanDalamNegeriResponseDto toResponseDto(PenempatanDalamNegeri entity) {
        return PenempatanDalamNegeriResponseDto.builder()
                .id(entity.getId())
                .bulan(entity.getBulan())
                .tahun(entity.getTahun())
                .nama(entity.getNama())
                .nik(entity.getNik())
                .alamat(entity.getAlamat())
                .desa(entity.getDesa())
                .kecamatan(entity.getKecamatan())
                .provinsi(entity.getProvinsi())
                .kabupatenKota(entity.getKabupatenKota())
                .email(entity.getEmail())
                .noHp(entity.getNoHp())
                .jenisKelamin(entity.getJenisKelamin())
                .pendidikan(entity.getPendidikan())
                .provinsiPenempatan(entity.getProvinsiPenempatan())
                .kabupatenKotaPenempatan(entity.getKabupatenKotaPenempatan())
                .namaPerusahaan(entity.getNamaPerusahaan())
                .nibPerusahaan(entity.getNibPerusahaan())
                .alamatPerusahaan(entity.getAlamatPerusahaan())
                .provinsiPerusahaan(entity.getProvinsiPerusahaan())
                .kabupatenKotaPerusahaan(entity.getKabupatenKotaPerusahaan())
                .namaJabatan(entity.getNamaJabatan())
                .lapanganUsaha(entity.getLapanganUsaha())
                .tanggalMulai(entity.getTanggalMulai())
                .gajiUpah(entity.getGajiUpah())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private PenempatanDalamNegeri toEntity(PenempatanDalamNegeriRequestDto dto) {
        return PenempatanDalamNegeri.builder()
                .bulan(dto.getBulan())
                .tahun(dto.getTahun())
                .nama(dto.getNama())
                .nik(dto.getNik())
                .alamat(dto.getAlamat())
                .desa(dto.getDesa())
                .kecamatan(dto.getKecamatan())
                .provinsi(dto.getProvinsi())
                .kabupatenKota(dto.getKabupatenKota())
                .email(dto.getEmail())
                .noHp(dto.getNoHp())
                .jenisKelamin(dto.getJenisKelamin())
                .pendidikan(dto.getPendidikan())
                .provinsiPenempatan(dto.getProvinsiPenempatan())
                .kabupatenKotaPenempatan(dto.getKabupatenKotaPenempatan())
                .namaPerusahaan(dto.getNamaPerusahaan())
                .nibPerusahaan(dto.getNibPerusahaan())
                .alamatPerusahaan(dto.getAlamatPerusahaan())
                .provinsiPerusahaan(dto.getProvinsiPerusahaan())
                .kabupatenKotaPerusahaan(dto.getKabupatenKotaPerusahaan())
                .namaJabatan(dto.getNamaJabatan())
                .lapanganUsaha(dto.getLapanganUsaha())
                .tanggalMulai(dto.getTanggalMulai())
                .gajiUpah(dto.getGajiUpah())
                .build();
    }
}