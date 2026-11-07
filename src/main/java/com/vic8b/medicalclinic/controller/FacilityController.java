package com.vic8b.medicalclinic.controller;

import com.vic8b.medicalclinic.command.facility.CreateFacilityCommand;
import com.vic8b.medicalclinic.dto.ErrorMessageDto;
import com.vic8b.medicalclinic.dto.doctor.DoctorDto;
import com.vic8b.medicalclinic.dto.facility.FacilityDto;
import com.vic8b.medicalclinic.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Facilities", description = "Facilities management endpoints")
@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    @Operation(summary = "Get facilities, optionally filtered by doctor")
    @ApiResponse(responseCode = "200", description = "Returned facilities")
    @GetMapping
    public Set<FacilityDto> getFacilities(@RequestParam(required = false) Long doctorId) {
        return facilityService.getFacilities(doctorId);
    }

    @Operation(summary = "Get a facility by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facility found"),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class)))
    })
    @GetMapping("/{facilityId}")
    public FacilityDto getFacilityById(@PathVariable Long facilityId) {
        return facilityService.getFacilityById(facilityId);
    }

    @Operation(summary = "Get doctors assigned to a facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all assigned doctors"),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class)))
    })
    @GetMapping("/{facilityId}/doctors")
    public Set<DoctorDto> getDoctorsForFacility(@PathVariable Long facilityId) {
        return facilityService.getDoctors(facilityId);
    }

    @Operation(summary = "Create facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facility created"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "Facility name is taken",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityDto addFacility(@Valid @RequestBody CreateFacilityCommand command) {
        return facilityService.addFacility(command);
    }

    @Operation(summary = "Remove a facility by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Facility has been removed"),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessageDto.class))})
    })
    @DeleteMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFacilityById(@PathVariable Long facilityId) {
        facilityService.removeFacilityById(facilityId);
    }
}
