package com.vic8b.medicalclinic.dto;

public record ErrorMessageDto(String message, int httpCode, String timestamp) {
}
