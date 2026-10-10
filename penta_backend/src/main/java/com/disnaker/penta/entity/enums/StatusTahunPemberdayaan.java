package com.disnaker.penta.entity.enums;

/** Status satu tahun di rekap kegiatan pemberdayaan (dihitung otomatis, tidak disimpan di database) */
public enum StatusTahunPemberdayaan {
    /** Ada minimal satu kegiatan di tahun itu */
    TERLAKSANA,
    /** Tahunnya sudah lewat dan tidak ada kegiatan */
    TERLEWAT,
    /** Tahun ini, belum ada kegiatan */
    BELUM_TERLAKSANA
}