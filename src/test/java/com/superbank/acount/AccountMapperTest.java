package com.superbank.acount;

import com.superbank.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class AccountMapperTest {

    @Autowired
    private AccountMapper sut;

    @Test
    void shouldMapAccountToEntity(){
        // given
        CreateAccountRequestDto requestDto = new CreateAccountRequestDto("123", BigDecimal.TEN);

        // when
        Account account = sut.toEntity(requestDto);

        // then
        assertEquals("123", account.getAccountNumber());
        assertEquals( BigDecimal.TEN, account.getBalance());
    }

    @Test
    void shouldReturnNullEntity() {
        assertNull(sut.toEntity(null));
    }

    @Test
    void shouldReturnNullDto() {
        assertNull(sut.toDto(null));
    }




}
