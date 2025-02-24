package com.superbank.accountstatement;

import com.superbank.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.superbank.TransactionFactory.generateTransactions;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionMapperTest {

    @Autowired
    private TransactionMapper sut;


    @Test
    void shouldMapTransactionToDto() {
        // given
        final Transaction transaction = new Transaction();
        transaction.setDescription("test");
        transaction.setAmount(BigDecimal.TEN);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        // when
        TransactionResponseDto dto = sut.toDto(transaction);

        // then
        assertEquals("test", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(transaction.getCreatedAt(), dto.getCreatedAt());


    }

    @Test
    void shouldMapTransactionsToDtos() {
        // given
        final LocalDateTime date = LocalDateTime.now();
        final List<Transaction> transactions = generateTransactions(date, 10);

        // when
        List<TransactionResponseDto> transactionDtos = sut.toDtos(transactions);

        // then
        assertEquals(transactions.size(), transactionDtos.size());
        for(int i = 0; i < transactionDtos.size(); i++) {
            assertEquals(date, transactionDtos.get(i).getCreatedAt());
            assertEquals(transactions.get(i).getAmount(), transactionDtos.get(i).getAmount());
            assertEquals(transactions.get(i).getDescription(), transactionDtos.get(i).getDescription());
        }
    }



}
