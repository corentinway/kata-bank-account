package com.superbank.withdrawal;

public class WithdrawalInsufficientBalanceException extends Exception {
    private final String accountNumber;

    public WithdrawalInsufficientBalanceException(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String getMessage() {
        return "Solde insuffisant sur le compte: " + accountNumber;
    }
}
