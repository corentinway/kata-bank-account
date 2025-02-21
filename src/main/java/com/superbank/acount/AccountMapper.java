package com.superbank.acount;

import com.superbank.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "accountNumber", source = "accountNumber")
    @Mapping(target = "balance", source = "balance")
    Account toEntity(CreateAccountRequestDto createAccountRequestDto);

    AccountDto toDto(Account account);
}
