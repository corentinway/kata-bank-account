package com.superbank.withdrawal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superbank.acount.AccountRepository;
import com.superbank.acount.TransactionRepository;
import com.superbank.model.Account;
import com.superbank.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WithdrawalControllerIntegrationTest {
    
    private static final String ACCOUNT_NUMBER = "" + System.currentTimeMillis();


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void shouldGetNotFoundHttpStatusGivenAccountNumberNotExisting() throws Exception {
        // Given
        final WithdrawalRequestDto withdrawalRequestDto = new WithdrawalRequestDto("666", BigDecimal.TEN);

        // When & Then
        mockMvc.perform(post("/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawalRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Account not found for account number: " + withdrawalRequestDto.accountNumber()));
    }

    @Test
    void shouldWithdrawMoneyToTheAccount() throws Exception {
        // Given
        final Account account = new Account();
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setBalance(new BigDecimal("100.00"));
        accountRepository.save(account);

        // When
        final WithdrawalRequestDto withdrawalRequestDto = new WithdrawalRequestDto(ACCOUNT_NUMBER, BigDecimal.TEN);

        mockMvc.perform(post("/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawalRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        final Account actualAccount = accountRepository.findAccountByAccountNumber(ACCOUNT_NUMBER).orElseThrow(AccountNotFoundException::new);
        final BigDecimal expectedAmount = new BigDecimal("90.00").setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(expectedAmount, actualAccount.getBalance());

        final List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        final Transaction transaction = transactions.get(0);
        assertEquals(ACCOUNT_NUMBER, transaction.getAccount().getAccountNumber());
        assertEquals(BigDecimal.TEN.setScale(2, RoundingMode.HALF_DOWN).negate(), transaction.getAmount());
        assertEquals("Withdraw of 10.00 €", transaction.getDescription());

    }

    @Test
    void shouldNotWithdrawMoneyToTheAccountGivenInsufficientBalance() throws Exception {
        // Given
        final Account account = new Account();
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setBalance(new BigDecimal("2.00"));
        accountRepository.save(account);

        // When
        final WithdrawalRequestDto withdrawalRequestDto = new WithdrawalRequestDto(ACCOUNT_NUMBER, BigDecimal.TEN);

        mockMvc.perform(post("/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawalRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Solde insuffisant sur le compte: " + ACCOUNT_NUMBER));

        final Account actualAccount = accountRepository.findAccountByAccountNumber(ACCOUNT_NUMBER).orElseThrow(AccountNotFoundException::new);
        final BigDecimal expectedAmount = new BigDecimal("2.00").setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(expectedAmount, actualAccount.getBalance());

        final List<Transaction> transactions = transactionRepository.findAll();
        assertTrue(transactions.isEmpty());

    }


    @Test
    void shouldNotWithdrawMoneyToTheAccountGivenNegativeAmount() throws Exception {
        // Given
        final Account account = new Account();
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        // When
        final WithdrawalRequestDto withdrawalRequestDto = new WithdrawalRequestDto(ACCOUNT_NUMBER, BigDecimal.TEN.negate());

        mockMvc.perform(post("/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawalRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"amount\":\"must be greater than 0.0\"}"));
    }
}
