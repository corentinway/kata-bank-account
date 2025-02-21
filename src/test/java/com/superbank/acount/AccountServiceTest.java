package com.superbank.acount;

import com.superbank.model.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService sut;

    public AccountServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        sut = new AccountService(accountRepository, AccountMapper.INSTANCE);
        accountRepository.deleteAll();
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        // Given
        final CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto("123456789", new BigDecimal("1000.00"));

        final Account account = new Account();
        account.setAccountNumber("123456789");
        account.setBalance(new BigDecimal("1000.00"));

        final AtomicReference<Account> accountToSaveReference = new AtomicReference<>();

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            final Account accountToSave = invocation.getArgument(0);
            accountToSave.setId(1L);

            accountToSaveReference.set(accountToSave);

            return accountToSave;
        });

        // When
        sut.createAccount(createAccountRequestDto);

        Account found = accountToSaveReference.get();

        // Then
        assertEquals(createAccountRequestDto.accountNumber(), found.getAccountNumber());
        assertEquals(createAccountRequestDto.balance(), found.getBalance());

    }

    @Test
    public void shouldThrowAccountNotFoundExceptionGivenNotPresentAccountNumber() {

        final String accountNumber = Integer.MAX_VALUE + LocalDate.now().toString();

        final AccountNotFoundException exception = Assertions.assertThrows(AccountNotFoundException.class, () -> sut.findAccount(accountNumber));

        assertEquals("Account not found for account number: "  + accountNumber, exception.getMessage());
    }



}