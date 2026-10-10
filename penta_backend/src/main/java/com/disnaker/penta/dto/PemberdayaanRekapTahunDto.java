package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.StatusTahunPemberdayaan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Satu baris di tabel "Rekap per tahun" */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PemberdayaanRekapTahunDto {

    private int tahun;
    private int jumlahKegiatan;
    private long totalPeserta;
    private int totalFoto;
    /** Tampilan "2 / 3" = laporanTerunggah / jumlahKegiatan */
    private int laporanTerunggah;
    private StatusTahunPemberdayaan status;
}