package com.superbank.acount;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "Object représentant un compte bancaire")
public class CreateAccountRequestDto {
        @Schema(description = "Identifiant du compte", example = "123456789", accessMode = Schema.AccessMode.READ_ONLY)
        @Size(max = 20, message = "Le numéro de compte ne peut pas dépasser 20 caractères")
        String accountNumber;

        @Schema(description = "Solde du compte bancaire", example = "1000.00", accessMode = Schema.AccessMode.READ_WRITE)
        BigDecimal balance;
}
