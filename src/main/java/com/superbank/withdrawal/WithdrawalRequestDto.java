package com.superbank.withdrawal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Objet représentant une demande de retrait")
public record WithdrawalRequestDto(

        @Schema(description = "Identifiant du compte", example = "123456789", accessMode = Schema.AccessMode.READ_ONLY)
        @NotNull
        @Size(max = 20, message = "Le numéro de compte ne peut pas dépasser 20 caractères")
        String accountNumber,

        @Schema(description = "montant (positif) à retirer du compte (s'il est disponible)")
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 10, fraction = 2)
        BigDecimal amount) {
}
