package com.superbank.deposit;

import com.superbank.acount.TransactionRepository;
import com.superbank.model.Account;
import com.superbank.acount.AccountNotFoundException;
import com.superbank.acount.AccountService;
import com.superbank.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit")
@Tag(name = "Deposit", description = "API pour gérer le dépot d'argent sur un compte")
public record DepositController(AccountService accountService, TransactionRepository transactionRepository) {

    @PostMapping
    @Operation(summary = "Effectuer un dépot d'argent sur un compte", description = "Effectue un dépot d'argent sur un compte bancaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dépot effectué avec succès", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Compte bancaire non trouvé", content = @Content(mediaType = "text")),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deposit(
            @RequestBody @Valid DepositRequestDto depositRequestDto) throws AccountNotFoundException {

        // validate account
        final String accountNumber = depositRequestDto.accountNumber();

        final Account account = accountService.findAccount(accountNumber);
        // simple solution, everything is done here.
        // if we add more checks, we can do a service


        account.setBalance(account.getBalance().add(depositRequestDto.amount()));

        final Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(depositRequestDto.amount());
        transactionRepository.save(transaction);

        accountService.updateAccount(account);

        return ResponseEntity.ok().build();
    }

}
