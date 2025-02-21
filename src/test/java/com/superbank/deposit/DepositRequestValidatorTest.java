package com.superbank.deposit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositRequestValidatorTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldNotValidateGivenNegativeAmount() {
        // Given
        DepositRequestDto requestDto = new DepositRequestDto("123456789", BigDecimal.TEN.negate());

        // When
        Set<ConstraintViolation<DepositRequestDto>> violations = validator.validate(requestDto);

        // Then
        assertEquals(1, violations.size());
        assertEquals("amount", violations.iterator().next().getPropertyPath().toString());

    }
}
