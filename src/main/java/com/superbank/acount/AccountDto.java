package com.superbank.acount;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Schema(description = "Object représentant un compte bancaire")
public record AccountDto(
        @Schema(description = "Identifiant du compte", example = "123456789", accessMode = Schema.AccessMode.READ_ONLY)
        @Size(max = 20, message = "Le numéro de compte ne peut pas dépasser 20 caractères")
        String accountNumber,

        @Schema(description = "Solde du compte bancaire", example = "1000.00", accessMode = Schema.AccessMode.READ_WRITE)
        BigDecimal balance,

        @Schema(description = "date de dernière mise à jours du compte")
        LocalDateTime updatedAt
        ) {
}
