package com.superbank.acount;

import com.superbank.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toEntity(CreateAccountRequestDto createAccountRequestDto) {
        if(createAccountRequestDto == null) {
            return null;
        }
        Account account = new Account();
        account.setAccountNumber(createAccountRequestDto.getAccountNumber());
        account.setBalance(createAccountRequestDto.getBalance());
        return account;
    }

    public AccountDto toDto(Account account) {
        if(account==null) {
            return null;
        }

        return new AccountDto(account.getAccountNumber(), account.getBalance(), account.getCreatedAt());
    }
}
