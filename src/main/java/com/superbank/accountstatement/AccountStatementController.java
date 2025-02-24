package com.superbank.accountstatement;

import com.superbank.acount.AccountNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/account-statement")
@Tag(name="account-statement", description = "API pour générer un relevé bancaire d'un compte")
public record AccountStatementController(AccountStatementService accountStatementService) {


    @PostMapping
    public AccountStatementDto createAccountStatement(
            @RequestBody @Valid AccountStatementRequestDto accountStatementRequestDto
    ) throws AccountNotFoundException {
        return accountStatementService.createAccountStatement(accountStatementRequestDto);
    }

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> createPDFAccountStatement(
            @RequestBody @Valid AccountStatementRequestDto accountStatementRequestDto
    ) throws AccountNotFoundException, IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            accountStatementService.createPDFAccountStatement(accountStatementRequestDto, outputStream);

            final String filename = "account_statement_" + accountStatementRequestDto.accountNumber() + "_" + accountStatementRequestDto.yearOfRequest() + "-" + accountStatementRequestDto.monthOfRequest() + ".pdf";

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("inline", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());

        }


    }


}
