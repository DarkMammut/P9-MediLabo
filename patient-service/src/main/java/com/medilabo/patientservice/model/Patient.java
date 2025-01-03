package com.medilabo.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Génération auto de l'ID (SQL)
    private Long id;

    @NotBlank(message = "First name is mandatory")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Date of birth is mandatory")
    @Column(nullable = false)
    private LocalDate birthdate;

    @NotNull(message = "Gender is mandatory")
    @Column(nullable = false)
    private String gender;

    @Column()
    private String address;

    @Column()
    private String phoneNumber;
}
