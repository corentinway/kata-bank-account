package com.superbank.accountstatement;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.superbank.acount.AccountNotFoundException;
import com.superbank.acount.AccountService;
import com.superbank.acount.TransactionRepository;
import com.superbank.model.Account;
import com.superbank.model.Transaction;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public record AccountStatementService(AccountService accountService, TransactionRepository transactionRepository,
                                      TransactionMapper transactionMapper) {

    public AccountStatementDto createAccountStatement(
            AccountStatementRequestDto accountStatementRequestDto
    ) throws AccountNotFoundException {

        final Account account = accountService.findAccount(accountStatementRequestDto.accountNumber());

        final LocalDate startDate = LocalDate.of(accountStatementRequestDto.yearOfRequest(), accountStatementRequestDto.monthOfRequest(), 1);
        final LocalDate endDate = startDate.plusMonths(1);

        final List<Transaction> transactions = transactionRepository.findAllByAccountAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(account, startDate.atStartOfDay(), endDate.atStartOfDay());

        final List<TransactionResponseDto> transactionResponses = transactionMapper.toDtos(transactions);

        return new AccountStatementDto(accountStatementRequestDto.accountNumber(), startDate, endDate.minusDays(1), transactionResponses);

    }

    public void createPDFAccountStatement(AccountStatementRequestDto accountStatementRequestDto, OutputStream outputStream) throws AccountNotFoundException {
        final AccountStatementDto statementDto = createAccountStatement(accountStatementRequestDto);

        final PdfWriter writer = new PdfWriter(outputStream);
        final PdfDocument pdfDocument = new PdfDocument(writer);
        final Document document = new Document(pdfDocument);

        final Paragraph title = new Paragraph("Account Statement")
                .setFontSize(18)
                .setFontColor(ColorConstants.RED)
                ;
        document.add(title);

        document.add(new Paragraph("Account Number: " + statementDto.accountNumber()));
        document.add(new Paragraph("Start Date: " + statementDto.startDate()));
        document.add(new Paragraph("End Date: " + statementDto.endDate()));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Transactions: "));

        Table table = new Table(3);
        table.addHeaderCell("Date");
        table.addHeaderCell("Description");
        table.addHeaderCell("Amount");

        statementDto.transactions().forEach( transaction -> {
            table.addCell(transaction.getCreatedAt().toLocalDate().toString());
            table.addCell(transaction.getDescription());
            table.addCell(transaction.getAmount().toString());
        });

        document.add(table);

        document.close();

    }
}
