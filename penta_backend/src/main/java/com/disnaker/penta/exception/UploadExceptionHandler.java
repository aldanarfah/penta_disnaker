package com.disnaker.penta.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

/**
 * Menangani file yang melebihi batas ukuran upload server.
 * Diberi prioritas tertinggi supaya diproses sebelum handler Exception umum di GlobalExceptionHandler.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UploadExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleTooLarge(MaxUploadSizeExceededException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(413)
                .message("Ukuran file melebihi batas (foto maksimal 5 MB, laporan maksimal 10 MB)")
                .build();
        return ResponseEntity.status(413).body(error);
    }
}