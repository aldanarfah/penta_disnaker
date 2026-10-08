package com.disnaker.penta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PmiDokumenResponseDto {
    private Long id;
    private Long pmiId;
    private String namaFile;
    private String jenisDokumen;
    private LocalDateTime uploadedAt;
    /** URL untuk download/lihat file ini */
    private String downloadUrl;
}
