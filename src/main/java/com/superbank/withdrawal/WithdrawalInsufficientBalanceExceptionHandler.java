package com.superbank.withdrawal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WithdrawalInsufficientBalanceExceptionHandler {

    @ExceptionHandler(WithdrawalInsufficientBalanceException.class)
    public ResponseEntity<String> handleWithdrawalInsufficientBalanceException(WithdrawalInsufficientBalanceException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
