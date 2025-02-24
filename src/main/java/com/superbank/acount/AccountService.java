package com.superbank.acount;

import com.superbank.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {

    public void createAccount(CreateAccountRequestDto createAccountRequestDto) {
        final Account account = accountMapper.toEntity(createAccountRequestDto);
        accountRepository.save(account);
    }

    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        return accountRepository.findAccountByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    public List<AccountDto> findAllAccounts() {
        return accountRepository.findAll().stream().map(accountMapper::toDto).toList();
    }

}
