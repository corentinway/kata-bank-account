package com.superbank.acount;

import com.superbank.model.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "API pour gérer les comptes bancaires")
public record AccountController(AccountService accountService) {

    @GetMapping
    @Operation(summary = "Obtenir tous les comptes", description = "Retourne une liste de tous les comptes existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des comptes retournée avec succès", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Liste vide", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json"))
    })
    public List<AccountDto> getAllAccounts() {
        return accountService.findAllAccounts();
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau compte", description = "Créer un compte banciare avec les informations fournies dans la demande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compte créé avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))
            ),
            @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(mediaType = "application/json"))
    })
    public void createAccount(
            @RequestBody
            @Valid
            CreateAccountRequestDto createAccountRequestDto) {
        accountService.createAccount(createAccountRequestDto);
    }

    @GetMapping("/{accountNumber}")
    public AccountDto getAccount(@PathVariable String accountNumber) throws AccountNotFoundException {
        return accountService.findAccountStatement(accountNumber);
    }


}
