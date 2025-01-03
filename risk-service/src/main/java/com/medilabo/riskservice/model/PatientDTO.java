package com.medilabo.riskservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // Active la méthode toBuilder
public class PatientDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthdate;

    private String gender;

    private String address;

    private String phoneNumber;
}

