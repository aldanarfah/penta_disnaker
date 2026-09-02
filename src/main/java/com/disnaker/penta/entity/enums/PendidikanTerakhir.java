package com.disnaker.penta.entity.enums;

public enum PendidikanTerakhir {
    SD, SMP,
    SMA_SMK, // direpresentasikan sebagai "SMA/SMK" di kolom database, lihat @Column converter jika diperlukan
    D1, D2, D3, D4,
    S1, S2, S3
}
