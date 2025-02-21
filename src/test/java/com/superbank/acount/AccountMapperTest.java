package com.superbank.acount;

import com.superbank.model.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountMapperTest {

    @Test
    void shouldMapAccountToEntity(){
        // given
        CreateAccountRequestDto requestDto = new CreateAccountRequestDto("123", BigDecimal.TEN);

        // when
        Account account = AccountMapper.INSTANCE.toEntity(requestDto);

        // then
        assertEquals("123", account.getAccountNumber());
        assertEquals( BigDecimal.TEN, account.getBalance());


    }


}
