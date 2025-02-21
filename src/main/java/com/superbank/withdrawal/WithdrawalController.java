package com.superbank.withdrawal;

import com.superbank.model.Account;
import com.superbank.acount.AccountNotFoundException;
import com.superbank.acount.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController("/withdrawal")
@Tag(name="widthdrawal", description = "API pour gérer le retrait d'argent sur un compte")
public record WithdrawalController(AccountService accountService) {

    @PostMapping
    @Operation(summary = "Effectuer un retrait d'argent sur un compte", description = "Effectue un retrait d'argent sur un compte bancaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrait effectué avec succès", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Compte bancaire non trouvé", content = @Content(mediaType = "text")),
            @ApiResponse(responseCode = "403", description = "Solde insuffisant sur le compte bancaire pour faire un retrait", content = @Content(mediaType = "text")),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> withdraw(
            @RequestBody @Valid WithdrawalRequestDto withdrawalRequestDto
    ) throws AccountNotFoundException, WithdrawalInsufficientBalanceException {

        // validate account
        final String accountNumber = withdrawalRequestDto.accountNumber();

        final Account account = accountService.findAccount(accountNumber);

        final BigDecimal newBalance = account.getBalance().subtract(withdrawalRequestDto.amount());

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new WithdrawalInsufficientBalanceException(accountNumber);
        }

        account.setBalance(newBalance);

        accountService.updateAccount(account);

        return ResponseEntity.ok().build();


    }



}
