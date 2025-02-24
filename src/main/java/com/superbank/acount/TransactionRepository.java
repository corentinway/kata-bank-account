package com.superbank.acount;

import com.superbank.model.Account;
import com.superbank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByAccountAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(Account account, LocalDateTime startDate, LocalDateTime endDate);
}
