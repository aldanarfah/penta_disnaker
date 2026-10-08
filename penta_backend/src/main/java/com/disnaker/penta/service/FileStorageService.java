package com.disnaker.penta.service;

import com.disnaker.penta.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Menangani penyimpanan file fisik ke folder server (bukan ke database).
 * Lokasi folder diatur lewat properti 'file.upload-dir' di application.properties.
 */
@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new FileStorageException("Tidak bisa membuat folder penyimpanan file: " + uploadDir, e);
        }
    }

    /**
     * Simpan file ke dalam subfolder tertentu (misal "pmi-dokumen"), dengan nama unik
     * supaya tidak bentrok kalau ada 2 file dengan nama asli yang sama.
     * @return path relatif file yang tersimpan (untuk dicatat ke database)
     */
    public String store(MultipartFile file, String subFolder) {
        if (file.isEmpty()) {
            throw new FileStorageException("File yang diupload kosong");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }

        // Nama unik: UUID + ekstensi asli, supaya tidak ada file saling menimpa
        String storedFilename = UUID.randomUUID() + extension;

        try {
            Path targetDir = this.rootLocation.resolve(subFolder).normalize();
            Files.createDirectories(targetDir);

            Path targetPath = targetDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return subFolder + "/" + storedFilename;
        } catch (IOException e) {
            throw new FileStorageException("Gagal menyimpan file: " + originalFilename, e);
        }
    }

    public Path resolve(String relativePath) {
        return this.rootLocation.resolve(relativePath).normalize();
    }

    public void delete(String relativePath) {
        try {
            Files.deleteIfExists(resolve(relativePath));
        } catch (IOException e) {
            throw new FileStorageException("Gagal menghapus file: " + relativePath, e);
        }
    }
}
