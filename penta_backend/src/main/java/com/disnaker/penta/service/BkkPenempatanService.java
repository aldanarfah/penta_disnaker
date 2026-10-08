package com.disnaker.penta.service;

import com.disnaker.penta.dto.BkkPenempatanRequestDto;
import com.disnaker.penta.dto.BkkPenempatanResponseDto;
import com.disnaker.penta.entity.BkkPenempatan;
import com.disnaker.penta.entity.BkkSekolah;
import com.disnaker.penta.entity.enums.PendidikanTerakhir;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.BkkPenempatanRepository;
import com.disnaker.penta.repository.BkkSekolahRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BkkPenempatanService {

    /**
     * Nilai TETAP untuk semua data BKK: khusus alumni SMK di Lumajang, Jawa Timur.
     * Tidak bisa ditimpa oleh client karena tidak ada di BkkPenempatanRequestDto.
     */
    private static final String FIXED_PROVINSI = "Jawa Timur";
    private static final String FIXED_KABUPATEN_KOTA = "Lumajang";
    private static final PendidikanTerakhir FIXED_PENDIDIKAN = PendidikanTerakhir.SMA_SMK;

    private final BkkPenempatanRepository bkkPenempatanRepository;
    private final BkkSekolahRepository bkkSekolahRepository;

    public List<BkkPenempatanResponseDto> findAll() {
        return bkkPenempatanRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public BkkPenempatanResponseDto findById(Long id) {
        BkkPenempatan entity = bkkPenempatanRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Penempatan BKK", id));
        return toResponseDto(entity);
    }

    public List<BkkPenempatanResponseDto> searchByNama(String nama) {
        return bkkPenempatanRepository.findByNamaContainingIgnoreCase(nama)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<BkkPenempatanResponseDto> searchByNik(String nik) {
        return bkkPenempatanRepository.findByNikContaining(nik)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<BkkPenempatanResponseDto> findBySekolah(Long sekolahId) {
        return bkkPenempatanRepository.findBySekolahId(sekolahId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public BkkPenempatanResponseDto create(BkkPenempatanRequestDto request) {
        BkkSekolah sekolah = bkkSekolahRepository.findById(request.getSekolahId())
                .orElseThrow(() -> ResourceNotFoundException.forId("Sekolah BKK", request.getSekolahId()));

        BkkPenempatan entity = BkkPenempatan.builder()
                .sekolah(sekolah)
                .nik(request.getNik())
                .nama(request.getNama())
                .jenisKelamin(request.getJenisKelamin())
                .kecamatan(request.getKecamatan())
                .desaKelurahan(request.getDesaKelurahan())
                .alamat(request.getAlamat())
                .provinsi(FIXED_PROVINSI)
                .kabupatenKota(FIXED_KABUPATEN_KOTA)
                .email(request.getEmail())
                .noHp(request.getNoHp())
                .pendidikanTerakhir(FIXED_PENDIDIKAN)
                .provinsiPenempatan(request.getProvinsiPenempatan())
                .kabupatenKotaPenempatan(request.getKabupatenKotaPenempatan())
                .namaPerusahaan(request.getNamaPerusahaan())
                .nibPerusahaan(request.getNibPerusahaan())
                .alamatPerusahaan(request.getAlamatPerusahaan())
                .provinsiPerusahaan(request.getProvinsiPerusahaan())
                .kabupatenKotaPerusahaan(request.getKabupatenKotaPerusahaan())
                .jabatan(request.getJabatan())
                .lapanganUsaha(request.getLapanganUsaha())
                .tglMulai(request.getTglMulai())
                .gajiUpah(request.getGajiUpah())
                .build();

        BkkPenempatan saved = bkkPenempatanRepository.save(entity);
        return toResponseDto(saved);
    }

    public BkkPenempatanResponseDto update(Long id, BkkPenempatanRequestDto request) {
        BkkPenempatan existing = bkkPenempatanRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data Penempatan BKK", id));

        BkkSekolah sekolah = bkkSekolahRepository.findById(request.getSekolahId())
                .orElseThrow(() -> ResourceNotFoundException.forId("Sekolah BKK", request.getSekolahId()));

        existing.setSekolah(sekolah);
        existing.setNik(request.getNik());
        existing.setNama(request.getNama());
        existing.setJenisKelamin(request.getJenisKelamin());
        existing.setKecamatan(request.getKecamatan());
        existing.setDesaKelurahan(request.getDesaKelurahan());
        existing.setAlamat(request.getAlamat());
        existing.setProvinsi(FIXED_PROVINSI);
        existing.setKabupatenKota(FIXED_KABUPATEN_KOTA);
        existing.setEmail(request.getEmail());
        existing.setNoHp(request.getNoHp());
        existing.setPendidikanTerakhir(FIXED_PENDIDIKAN);
        existing.setProvinsiPenempatan(request.getProvinsiPenempatan());
        existing.setKabupatenKotaPenempatan(request.getKabupatenKotaPenempatan());
        existing.setNamaPerusahaan(request.getNamaPerusahaan());
        existing.setNibPerusahaan(request.getNibPerusahaan());
        existing.setAlamatPerusahaan(request.getAlamatPerusahaan());
        existing.setProvinsiPerusahaan(request.getProvinsiPerusahaan());
        existing.setKabupatenKotaPerusahaan(request.getKabupatenKotaPerusahaan());
        existing.setJabatan(request.getJabatan());
        existing.setLapanganUsaha(request.getLapanganUsaha());
        existing.setTglMulai(request.getTglMulai());
        existing.setGajiUpah(request.getGajiUpah());

        BkkPenempatan updated = bkkPenempatanRepository.save(existing);
        return toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!bkkPenempatanRepository.existsById(id)) {
            throw ResourceNotFoundException.forId("Data Penempatan BKK", id);
        }
        bkkPenempatanRepository.deleteById(id);
    }

    // ==================== Mapper Entity <-> DTO ====================

    private BkkPenempatanResponseDto toResponseDto(BkkPenempatan entity) {
        BkkSekolah sekolah = entity.getSekolah();
        return BkkPenempatanResponseDto.builder()
                .id(entity.getId())
                .sekolahId(sekolah != null ? sekolah.getId() : null)
                .namaSekolah(sekolah != null ? sekolah.getNamaSekolah() : null)
                .nik(entity.getNik())
                .nama(entity.getNama())
                .jenisKelamin(entity.getJenisKelamin())
                .kecamatan(entity.getKecamatan())
                .desaKelurahan(entity.getDesaKelurahan())
                .alamat(entity.getAlamat())
                .provinsi(entity.getProvinsi())
                .kabupatenKota(entity.getKabupatenKota())
                .email(entity.getEmail())
                .noHp(entity.getNoHp())
                .pendidikanTerakhir(entity.getPendidikanTerakhir())
                .provinsiPenempatan(entity.getProvinsiPenempatan())
                .kabupatenKotaPenempatan(entity.getKabupatenKotaPenempatan())
                .namaPerusahaan(entity.getNamaPerusahaan())
                .nibPerusahaan(entity.getNibPerusahaan())
                .alamatPerusahaan(entity.getAlamatPerusahaan())
                .provinsiPerusahaan(entity.getProvinsiPerusahaan())
                .kabupatenKotaPerusahaan(entity.getKabupatenKotaPerusahaan())
                .jabatan(entity.getJabatan())
                .lapanganUsaha(entity.getLapanganUsaha())
                .tglMulai(entity.getTglMulai())
                .gajiUpah(entity.getGajiUpah())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}