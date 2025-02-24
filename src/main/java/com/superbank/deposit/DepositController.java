package com.superbank.deposit;

import com.superbank.acount.AccountNotFoundException;
import com.superbank.acount.TransactionService;
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
public record DepositController(TransactionService transactionService) {

    @PostMapping
    @Operation(summary = "Effectuer un dépot d'argent sur un compte", description = "Effectue un dépot d'argent sur un compte bancaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dépot effectué avec succès", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Compte bancaire non trouvé", content = @Content(mediaType = "text")),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deposit(
            @RequestBody @Valid DepositRequestDto depositRequestDto) throws AccountNotFoundException {

      transactionService.deposit(depositRequestDto);

        return ResponseEntity.ok().build();
    }

}
