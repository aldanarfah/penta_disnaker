package com.disnaker.penta.dto;

import com.disnaker.penta.entity.enums.StatusBulanKomunitas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KomunitasRekapBulanDto {

    /** 1 sampai 12 */
    private int bulan;
    private String namaBulan;
    private StatusBulanKomunitas status;
    /** Kegiatan di bulan ini, urut dari tanggal paling awal. Kosong kalau tidak ada kegiatan. */
    private List<KomunitasRekapKegiatanDto> kegiatan;
}