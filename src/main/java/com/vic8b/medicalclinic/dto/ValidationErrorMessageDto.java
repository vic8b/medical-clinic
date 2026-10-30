package com.vic8b.medicalclinic.dto;

import java.util.List;

public record ValidationErrorMessageDto(String message, int httpCode, String timestamp, List<String> errors) {
}
