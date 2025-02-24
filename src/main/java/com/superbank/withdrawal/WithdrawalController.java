package com.superbank.withdrawal;

import com.superbank.acount.AccountNotFoundException;
import com.superbank.acount.TransactionService;
import com.superbank.acount.withdraw.NotEnoughFundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/withdrawal")
@Tag(name="widthdrawal", description = "API pour gérer le retrait d'argent sur un compte")
public record WithdrawalController(TransactionService transactionService ) {

    @PostMapping
    @Operation(summary = "Effectuer un retrait d'argent sur un compte", description = "Effectue un retrait d'argent sur un compte bancaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrait effectué avec succès", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Compte bancaire non trouvé", content = @Content(mediaType = "text")),
            @ApiResponse(responseCode = "403", description = "Solde insuffisant sur le compte bancaire pour faire un retrait", content = @Content(mediaType = "text")),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> withdraw(
            @RequestBody @Valid WithdrawalRequestDto withdrawalRequestDto
    ) throws AccountNotFoundException {
        try {
            transactionService.withdraw(withdrawalRequestDto);
        } catch (NotEnoughFundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }



}
