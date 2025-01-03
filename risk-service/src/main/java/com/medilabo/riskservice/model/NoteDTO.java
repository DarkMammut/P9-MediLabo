package com.medilabo.riskservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true) // Active la méthode toBuilder
public class NoteDTO {

    private String id;

    private int patId;

    private String patient;

    private String note;
}
