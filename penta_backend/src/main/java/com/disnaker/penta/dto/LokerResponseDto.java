package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.JenisKelaminLoker;
import com.disnaker.penta.entity.enums.PendidikanBps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LokerResponseDto {

    private Long id;
    private Integer bulan;
    private Integer tahun;
    private String namaPerusahaan;
    private String nibPerusahaan;
    private String lapanganUsaha;
    private String alamatPerusahaan;
    private String desa;
    private String kecamatan;
    private String provinsiPerusahaan;
    private String kabupatenKotaPerusahaan;
    private String kodeJabatan;
    private String namaJabatan;
    private Integer jumlahDibutuhkan;
    private JenisKelaminLoker jenisKelamin;
    private PendidikanBps pendidikan;
    private String keterampilanKompetensi;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}