package com.vic8b.medicalclinic.handler;

import com.vic8b.medicalclinic.dto.ErrorMessageDto;
import com.vic8b.medicalclinic.dto.ValidationErrorMessageDto;
import com.vic8b.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ErrorMessageDto> handleUnexpectedException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageDto("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now().format(FORMAT)));
    }

    @ExceptionHandler(MedicalClinicException.class)
    public ResponseEntity<ErrorMessageDto> handleMedicalClinicException(MedicalClinicException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ErrorMessageDto(exception.getMessage(), exception.getStatus().value(), LocalDateTime.now().format(FORMAT)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorMessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorMessageDto("Validation failed", HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now().format(FORMAT), errors));

    }
}
