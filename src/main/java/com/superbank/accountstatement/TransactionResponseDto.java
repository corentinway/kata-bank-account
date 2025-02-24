package com.superbank.accountstatement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "objet représentant une transaction")
public class TransactionResponseDto {
        @Schema(description = "Description de la transaction", example = "Dépot de 10 €", accessMode = Schema.AccessMode.READ_ONLY)
        @NotNull
        String description;

        @Schema(description = "Montant de la transaction", example = "10.00", accessMode = Schema.AccessMode.READ_ONLY)
        BigDecimal amount;

        @Schema(description = "date de la transaction", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime createdAt;
}
