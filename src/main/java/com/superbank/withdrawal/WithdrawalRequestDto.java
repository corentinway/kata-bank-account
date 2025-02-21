package com.superbank.withdrawal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record WithdrawalRequestDto(

        @NotNull
        @Size(max = 20, message = "Le numéro de compte ne peut pas dépasser 20 caractères")
        String accountNumber,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 10, fraction = 2)
        BigDecimal amount) {
}
