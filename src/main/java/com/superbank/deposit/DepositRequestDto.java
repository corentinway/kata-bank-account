package com.superbank.deposit;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DepositRequestDto(

        @NotNull
        @Size(max = 20, message = "Le numéro de compte ne peut pas dépasser 20 caractères")
        String accountNumber,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 10, fraction = 2)
        BigDecimal amount) {
}
