package com.medilabo.riskservice.service;

import com.medilabo.riskservice.model.NoteDTO;
import com.medilabo.riskservice.model.PatientDTO;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Stream;

@Service
public class RiskAssessmentService {

    private static String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // Supprime les marques diacritiques (accents)
    }

    private static final List<String> RISK_KEYWORDS = Stream.of(
                    "hémoglobine A1C", "microalbumine", "taille", "poids",
                    "fumeur", "fumeuse", "anormal", "cholestérol", "vertige",
                    "rechute", "réaction", "anticorps"
            )
            .map(String::toLowerCase) // Convertit en minuscule
            .map(RiskAssessmentService::normalize) // Supprime les accents
            .toList();

    public String assessRisk(PatientDTO patient, List<NoteDTO> notes) {
        long riskFactor = notes.stream()
                .map(NoteDTO::getNote)
                .map(String::toLowerCase) // Convertit en minuscule
                .map(RiskAssessmentService::normalize) // Normalise pour supprimer les accents
                .flatMap(note -> RISK_KEYWORDS.stream()
                        .filter(note::contains))
                .distinct()
                .count();

        int age = calculateAge(patient.getBirthdate());
        String gender = patient.getGender();

        System.out.println("Mots clés trouvés : " + riskFactor);
        System.out.println("Âge : " + age + ", Genre : " + gender);

        // Logique d'évaluation inchangée
        if (riskFactor == 0) {
            return "None";
        } else {
            if (age < 30) {
                if (gender.equals("M")) {
                    return switch ((int) riskFactor) {
                        case 1, 2 -> "None";
                        case 3, 4 -> "In Danger";
                        default -> "Early onset";
                    };
                } else {
                    return switch ((int) riskFactor) {
                        case 1, 2, 3 -> "None";
                        case 4, 5, 6 -> "In Danger";
                        default -> "Early onset";
                    };
                }
            } else {
                return switch ((int) riskFactor) {
                    case 1 -> "None";
                    case 2, 3, 4, 5 -> "Borderline";
                    case 6, 7 -> "In Danger";
                    default -> "Early onset";
                };
            }
        }
    }

    private int calculateAge(LocalDate birthdate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthdate, currentDate).getYears();
    }
}
