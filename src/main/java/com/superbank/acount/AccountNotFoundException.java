package com.superbank.acount;

import java.io.Serial;

public class AccountNotFoundException extends Exception{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String accountNumber;

    public AccountNotFoundException(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String getMessage() {
        return "Account not found for account number: " + accountNumber;
    }
}
