package com.vic8b.medicalclinic.dto;

import java.util.List;

public record ErrorMessageDto(String message, int httpCode, String timestamp, List<String> errors) {
    public static ErrorMessageDto of(String message, int httpCode, String timestamp) {
        return new ErrorMessageDto(message, httpCode, timestamp, List.of());
    }

    public static ErrorMessageDto withErrors(String message, int httpCode, String timestamp, List<String> errors) {
        return new ErrorMessageDto(message, httpCode, timestamp, errors);
    }
}
