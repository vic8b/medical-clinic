package com.vic8b.medicalclinic.handler;

import com.vic8b.medicalclinic.dto.ErrorMessageDto;
import com.vic8b.medicalclinic.exception.MedicalClinicException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy'T'HH:mm:ss");

    @ExceptionHandler(Exception.class)
    public ErrorMessageDto handleUnexpectedException(Exception exception, HttpServletResponse response) {
        int exceptionStatusValue = HttpStatus.INTERNAL_SERVER_ERROR.value();
        response.setStatus(exceptionStatusValue);
        return ErrorMessageDto.of("Unknown error", exceptionStatusValue, getCurrentTimeStamp());
    }

    @ExceptionHandler(MedicalClinicException.class)
    public ErrorMessageDto handleMedicalClinicException(MedicalClinicException exception, HttpServletResponse response) {
        int exceptionStatusValue = exception.getStatus().value();
        response.setStatus(exceptionStatusValue);
        return ErrorMessageDto.of(exception.getMessage(), exceptionStatusValue, getCurrentTimeStamp());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessageDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletResponse response) {
        int exceptionStatusValue = HttpStatus.BAD_REQUEST.value();
        response.setStatus(exceptionStatusValue);
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        return ErrorMessageDto.withErrors("Validation failed", exceptionStatusValue,
                getCurrentTimeStamp(), errors);
    }

    private String getCurrentTimeStamp() {
        return LocalDateTime.now().format(FORMAT);
    }
}
