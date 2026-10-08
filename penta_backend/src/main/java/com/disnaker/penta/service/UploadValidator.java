package com.disnaker.penta.service;

import com.disnaker.penta.exception.BusinessRuleException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Set;

/**
 * Pemeriksaan jenis dan ukuran file upload (foto dan laporan komunitas).
 * Pelanggaran dilaporkan sebagai BusinessRuleException (400), bukan error server.
 */
public final class UploadValidator {

    private static final long MAKS_FOTO = 5L * 1024 * 1024;
    private static final long MAKS_LAPORAN = 10L * 1024 * 1024;
    private static final Set<String> EKSTENSI_FOTO = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> EKSTENSI_LAPORAN = Set.of("pdf", "doc", "docx");

    private UploadValidator() {
    }

    public static void validasiFoto(MultipartFile file) {
        periksa(file, EKSTENSI_FOTO, MAKS_FOTO, "Foto", "JPG, PNG, atau WEBP", "5 MB");
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new BusinessRuleException("File '" + namaAman(file) + "' bukan gambar");
        }
    }

    public static void validasiLaporan(MultipartFile file) {
        periksa(file, EKSTENSI_LAPORAN, MAKS_LAPORAN, "Laporan", "PDF atau Word (.doc, .docx)", "10 MB");
    }

    /** Nama file asli tanpa folder, maksimal 255 karakter (potong dari depan supaya ekstensi aman) */
    public static String namaAman(MultipartFile file) {
        String nama = null;
        if (file.getOriginalFilename() != null) {
            nama = StringUtils.getFilename(StringUtils.cleanPath(file.getOriginalFilename()));
        }
        if (nama == null || nama.isBlank()) {
            nama = "file";
        }
        return nama.length() > 255 ? nama.substring(nama.length() - 255) : nama;
    }

    private static void periksa(MultipartFile file, Set<String> ekstensiBoleh, long maksBytes,
                                String jenis, String formatBoleh, String maksLabel) {
        if (file == null || file.isEmpty()) {
            throw new BusinessRuleException(jenis + " yang diunggah kosong");
        }
        String nama = namaAman(file);
        if (file.getSize() > maksBytes) {
            throw new BusinessRuleException(jenis + " '" + nama + "' terlalu besar, maksimal " + maksLabel);
        }
        int titik = nama.lastIndexOf('.');
        String ekstensi = titik >= 0 ? nama.substring(titik + 1).toLowerCase(Locale.ROOT) : "";
        if (!ekstensiBoleh.contains(ekstensi)) {
            throw new BusinessRuleException("Format " + jenis.toLowerCase(Locale.ROOT) + " harus " + formatBoleh);
        }
    }
}