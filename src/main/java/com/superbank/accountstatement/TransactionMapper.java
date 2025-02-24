package com.superbank.accountstatement;

import com.superbank.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {

    public TransactionResponseDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionResponseDto(
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );
    }

    public List<TransactionResponseDto> toDtos(List<Transaction> transactions) {
        if (transactions == null) {
            return null;
        }
        return transactions.stream().map(this::toDto).toList();
    }
}
