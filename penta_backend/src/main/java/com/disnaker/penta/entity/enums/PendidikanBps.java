package com.disnaker.penta.entity.enums;

/**
 * Kategori pendidikan gaya BPS (10 tingkat), dipakai di modul Loker dan
 * Penempatan Dalam Negeri. Granularitasnya beda dari PendidikanTerakhir
 * (SMA dan SMK dipisah di sini, sedangkan D4/S1 digabung jadi SARJANA),
 * sehingga sengaja dibuat enum terpisah, bukan reuse PendidikanTerakhir.
 */
public enum PendidikanBps {
    TIDAK_SEKOLAH,
    SD_SEDERAJAT,
    SMP_SEDERAJAT,
    SMA,
    SMK,
    DIPLOMA,
    SARJANA,
    MAGISTER,
    DOKTOR,
    TIDAK_TERIDENTIFIKASI
}