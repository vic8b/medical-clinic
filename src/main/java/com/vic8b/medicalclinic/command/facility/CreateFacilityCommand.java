package com.vic8b.medicalclinic.command.facility;

import jakarta.validation.constraints.NotBlank;

public record CreateFacilityCommand(@NotBlank String name, @NotBlank String city, @NotBlank String postalCode,
                                    @NotBlank String street, @NotBlank String buildingNumber) {
}
