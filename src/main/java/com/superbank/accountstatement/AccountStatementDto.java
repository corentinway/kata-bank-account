package com.superbank.accountstatement;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@Schema(description = "objet représentant un relevé de compta bancaire")
public record AccountStatementDto(
        @Schema(description = "Identifiant du compte", example = "123456789", accessMode = Schema.AccessMode.READ_ONLY)
        @NotNull
        @Size(max = 20, message = "Le numéro de compte ne peut pas dépasser 20 caractères")
        String accountNumber,

        @Schema(description = "Date du début du relevé, le début du mois demandé", example = "2022-01-01", accessMode = Schema.AccessMode.READ_ONLY)
        @NotNull
        LocalDate startDate,

        @Schema(description = "Date du début du relevé, le début du mois demandé", example = "2022-01-01", accessMode = Schema.AccessMode.READ_ONLY)
        @NotNull
        LocalDate endDate,

        @ArraySchema(schema = @Schema(implementation = TransactionResponseDto.class))
        List<TransactionResponseDto> transactions
) {
}
