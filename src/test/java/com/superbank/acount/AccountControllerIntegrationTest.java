package com.superbank.acount;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superbank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void shouldGetAllAccounts() throws Exception {
        // Given
        Account account1 = new Account();
        account1.setAccountNumber("12345");
        account1.setBalance(new BigDecimal("500.0"));

        Account account2 = new Account();
        account2.setAccountNumber("6789");
        account2.setBalance(new BigDecimal("1000.0"));

        accountRepository.saveAll(List.of(account1, account2));

        // When & Then
        mockMvc.perform(get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountNumber", is("12345")))
                .andExpect(jsonPath("$[1].accountNumber", is("6789")))
                .andExpect(jsonPath("$[1].updatedAt", notNullValue()))
        ;
    }

    @Test
    void shouldCreateAccount() throws Exception {
        // Given
        CreateAccountRequestDto newAccount = new CreateAccountRequestDto("98765", new BigDecimal("200.0"));

        // When & Then
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // Verify that the account was actually saved
        List<Account> accountsInDb = accountRepository.findAll();

        assertEquals(1, accountsInDb.size());
        assertEquals("98765", accountsInDb.get(0).getAccountNumber());
        assertNotNull(accountsInDb.get(0).getCreatedAt());
        assertNotNull(accountsInDb.get(0).getUpdatedAt());

    }


}