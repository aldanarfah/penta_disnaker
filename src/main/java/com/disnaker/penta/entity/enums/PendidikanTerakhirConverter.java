package com.disnaker.penta.entity.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Kolom pendidikan_terakhir di database bernilai literal 'SMA/SMK',
 * yang tidak valid sebagai nama konstanta enum Java (karena mengandung '/').
 * Converter ini menjembatani PendidikanTerakhir.SMA_SMK <-> "SMA/SMK" saat baca/tulis ke DB.
 */
@Converter(autoApply = true)
public class PendidikanTerakhirConverter implements AttributeConverter<PendidikanTerakhir, String> {

    @Override
    public String convertToDatabaseColumn(PendidikanTerakhir attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute == PendidikanTerakhir.SMA_SMK ? "SMA/SMK" : attribute.name();
    }

    @Override
    public PendidikanTerakhir convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        if ("SMA/SMK".equals(dbData)) {
            return PendidikanTerakhir.SMA_SMK;
        }
        return PendidikanTerakhir.valueOf(dbData);
    }
}
