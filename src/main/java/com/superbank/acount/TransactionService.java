package com.superbank.acount;

import com.superbank.acount.withdraw.NotEnoughFundException;
import com.superbank.deposit.DepositRequestDto;
import com.superbank.model.Account;
import com.superbank.model.Transaction;
import com.superbank.withdrawal.WithdrawalRequestDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public record TransactionService(AccountService accountService, TransactionRepository transactionRepository) {

    public void withdraw(WithdrawalRequestDto withdrawalRequestDto) throws AccountNotFoundException, NotEnoughFundException {
        final String accountNumber = withdrawalRequestDto.accountNumber();

        final Account account = accountService.findAccount(accountNumber);

        final BigDecimal newBalance = account.getBalance().subtract(withdrawalRequestDto.amount());

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughFundException(accountNumber);
        }

        account.setBalance(newBalance);

        final BigDecimal amount = withdrawalRequestDto.amount();
        createTransaction(account, "Withdraw of " + amount.setScale(2, RoundingMode.HALF_DOWN) + " €", amount.negate() );

        accountService.updateAccount(account);
    }


    public void deposit(DepositRequestDto depositRequestDto) throws AccountNotFoundException {
        final Account account = accountService.findAccount(depositRequestDto.accountNumber());
        // simple solution, everything is done here.
        // if we add more checks, we can do a service

        account.setBalance(account.getBalance().add(depositRequestDto.amount()));

        final BigDecimal amount = depositRequestDto.amount();
        createTransaction(account, "Deposit of " + amount.setScale(2, RoundingMode.HALF_DOWN) + " €", amount );

        accountService.updateAccount(account);
    }


    private void createTransaction(Account account, String description, BigDecimal amount) {
        final Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setDescription(description);
        transaction.setAmount(amount);

        transactionRepository.save(transaction);
    }



}
