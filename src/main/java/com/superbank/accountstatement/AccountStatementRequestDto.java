package com.superbank.accountstatement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Objet représentant un demande d'un relevé bancaire pour un compte sur un mois préci")
public record AccountStatementRequestDto(
        @Schema(description = "Identifiant du compte", example = "123456789", accessMode = Schema.AccessMode.READ_ONLY)
        @NotNull
        @Size(max = 20, message = "Le numéro de compte ne peut pas dépasser 20 caractères")
        String accountNumber,

        @Schema(description = "Mois du relevé bancaire", example = "12", accessMode = Schema.AccessMode.READ_ONLY)
        @NotNull
        @Min(1)
        @Max(12)
        int monthOfRequest,

        @Schema(description = "Années du relevé bancaire", example = "2022", accessMode = Schema.AccessMode.READ_ONLY)
        @NotNull
        @Min(1900)
        int yearOfRequest


) {

}
