package com.superbank.acount.withdraw;

public class NotEnoughFundException extends Throwable {
    private final String accountNumber;

    public NotEnoughFundException(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String getMessage() {
        return "Solde insuffisant sur le compte: " + accountNumber;
    }
}
