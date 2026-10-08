package com.disnaker.penta.service;

import com.disnaker.penta.dto.PmiDokumenResponseDto;
import com.disnaker.penta.entity.Pmi;
import com.disnaker.penta.entity.PmiDokumen;
import com.disnaker.penta.exception.ResourceNotFoundException;
import com.disnaker.penta.repository.PmiDokumenRepository;
import com.disnaker.penta.repository.PmiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PmiDokumenService {

    private static final String SUB_FOLDER = "pmi-dokumen";

    private final PmiDokumenRepository pmiDokumenRepository;
    private final PmiRepository pmiRepository;
    private final FileStorageService fileStorageService;

    public List<PmiDokumenResponseDto> findByPmiId(Long pmiId) {
        // Pastikan data PMI induknya memang ada, supaya error jelas kalau id salah
        if (!pmiRepository.existsById(pmiId)) {
            throw ResourceNotFoundException.forId("Data PMI", pmiId);
        }
        return pmiDokumenRepository.findByPmiId(pmiId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public PmiDokumenResponseDto upload(Long pmiId, MultipartFile file, String jenisDokumen) {
        Pmi pmi = pmiRepository.findById(pmiId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Data PMI", pmiId));

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        String pathFile = fileStorageService.store(file, SUB_FOLDER);

        PmiDokumen dokumen = PmiDokumen.builder()
                .pmi(pmi)
                .namaFile(originalFilename)
                .pathFile(pathFile)
                .jenisDokumen(jenisDokumen)
                .build();

        PmiDokumen saved = pmiDokumenRepository.save(dokumen);
        return toResponseDto(saved);
    }

    public void delete(Long dokumenId) {
        PmiDokumen dokumen = pmiDokumenRepository.findById(dokumenId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Dokumen PMI", dokumenId));

        fileStorageService.delete(dokumen.getPathFile());
        pmiDokumenRepository.delete(dokumen);
    }

    public PmiDokumen findEntityById(Long dokumenId) {
        return pmiDokumenRepository.findById(dokumenId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Dokumen PMI", dokumenId));
    }

    private PmiDokumenResponseDto toResponseDto(PmiDokumen entity) {
        return PmiDokumenResponseDto.builder()
                .id(entity.getId())
                .pmiId(entity.getPmi().getId())
                .namaFile(entity.getNamaFile())
                .jenisDokumen(entity.getJenisDokumen())
                .uploadedAt(entity.getUploadedAt())
                .downloadUrl("/api/pmi/dokumen/" + entity.getId() + "/download")
                .build();
    }
}
