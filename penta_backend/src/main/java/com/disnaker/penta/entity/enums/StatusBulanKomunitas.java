package com.disnaker.penta.entity.enums;

/** Status satu bulan di rekap kegiatan komunitas (dihitung otomatis, tidak disimpan di database) */
public enum StatusBulanKomunitas {
    /** Ada minimal satu kegiatan di bulan itu */
    TERLAKSANA,
    /** Bulannya sudah lewat dan tidak ada kegiatan */
    TERLEWAT,
    /** Bulan ini, belum ada kegiatan */
    BELUM_TERLAKSANA,
    /** Bulan yang belum tiba */
    BELUM_TIBA
}