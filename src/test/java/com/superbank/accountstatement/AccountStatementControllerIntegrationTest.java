package com.superbank.accountstatement;

import com.fasterxml.jackson.databind.JsonNode;
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

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static com.superbank.TransactionFactory.generateTransactions;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AccountStatementControllerIntegrationTest {


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
    void shouldGetAccountStatement() throws Exception {
        // Given
        final String accountNumber = "123456789";
        final Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        final LocalDateTime now = LocalDateTime.of(2025, 2, 22, 0, 0);
        final List<Transaction> transactions = generateTransactions(account, now, 20);
        transactionRepository.saveAll(transactions);

        final AccountStatementRequestDto request = new AccountStatementRequestDto("123456789", 2, 2025);

        // When & Then
        final String jsonResponse = mockMvc.perform(post("/account-statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is("123456789")))
                .andExpect(jsonPath("$.startDate", is("2025-02-01")))
                .andExpect(jsonPath("$.endDate", is("2025-02-28")))
                .andExpect(jsonPath("$.transactions", hasSize(20)))
                .andReturn()
                .getResponse()
                .getContentAsString()
        ;
        final JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        final List<TransactionResponseDto> actualTransactions = objectMapper.readValue(jsonNode.get("transactions").toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, TransactionResponseDto.class));

        assertEquals(transactions.size(), actualTransactions.size());
        for (int i = 0; i < transactions.size(); i++) {
            final Transaction expectedTransaction = transactions.get(i);
            final TransactionResponseDto actualTransaction = actualTransactions.get(i);
            assertEquals(expectedTransaction.getDescription(), actualTransaction.getDescription());
            assertEquals(expectedTransaction.getAmount().setScale(2, RoundingMode.HALF_DOWN), actualTransaction.getAmount().setScale(2, RoundingMode.HALF_DOWN));
            assertEquals(expectedTransaction.getCreatedAt().withNano(0), actualTransaction.getCreatedAt().withNano(0));
        }
    }

    @Test
    void shouldGetPdfAccountStatement() throws Exception {
        // Given
        final String accountNumber = "123456789";
        final Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        final LocalDateTime now = LocalDateTime.of(2025, 2, 22, 0, 0);
        final List<Transaction> transactions = generateTransactions(account, now, 20);
        transactionRepository.saveAll(transactions);

        final AccountStatementRequestDto request = new AccountStatementRequestDto("123456789", 2, 2025);

        // When & Then
        final byte[] bytes = mockMvc.perform(post("/account-statement/pdf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray()
        ;

        File file = File.createTempFile("test", ".pdf");
        Path path = Paths.get(file.getAbsolutePath());
        System.out.println("PDF path " + path.toAbsolutePath());

        Files.write(path, bytes);

        System.out.println("Fichier écrit avec succès" + file.getAbsolutePath() + " " + file.length() + " octets" );

    }

}
