package com.superbank;

import com.superbank.model.Account;
import com.superbank.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionFactory {

    public static List<Transaction> generateTransactions(LocalDateTime date, int count) {
        return generateTransactions(null, date, count);
    }

    public static List<Transaction> generateTransactions(Account account, LocalDateTime date, int count) {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Transaction transaction = new Transaction();

            // Ajouter des valeurs fictives pour les champs
            transaction.setAccount(account);
            transaction.setAmount(BigDecimal.valueOf(100 + i)); // Montant aléatoire ou fixe
            transaction.setCreatedAt(date); // Date commune
            transaction.setUpdatedAt(date); // Date commune par défaut
            transaction.setDescription("Transaction " + i);

            // Ajouter à la liste
            transactions.add(transaction);
        }

        return transactions;
    }

}
