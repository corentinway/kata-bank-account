package com.superbank.deposit;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DepositControllerIntegrationTest {

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
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void shouldGetNotFoundHttpStatusGivenAccountNumberNotExisting() throws Exception {
        // Given
        final DepositRequestDto depositRequestDto = new DepositRequestDto("666", BigDecimal.TEN);

        // When & Then
        mockMvc.perform(post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Account not found for account number: " + depositRequestDto.accountNumber()));
    }

    @Test
    void shouldAddMoneyToTheAccount() throws Exception {
        // Given
        final String accountNumber = "123456789";
        final Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        // When
        final DepositRequestDto depositRequestDto = new DepositRequestDto(accountNumber, BigDecimal.TEN);

        mockMvc.perform(post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        final  Account actualAccount = accountRepository.findAccountByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
        final BigDecimal expectedAmount = BigDecimal.TEN.setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(expectedAmount, actualAccount.getBalance());

        final List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        final Transaction transaction = transactions.get(0);
        assertEquals(accountNumber, transaction.getAccount().getAccountNumber());
        assertEquals(expectedAmount, transaction.getAmount());


    }


    @Test
    void shouldNotAddMoneyToTheAccountGivenNegativeAmount() throws Exception {
        // Given
        final String accountNumber = "123456789";
        final Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        // When
        final DepositRequestDto depositRequestDto = new DepositRequestDto(accountNumber, BigDecimal.TEN.negate());

        mockMvc.perform(post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"amount\":\"must be greater than 0.0\"}"));
    }


}
