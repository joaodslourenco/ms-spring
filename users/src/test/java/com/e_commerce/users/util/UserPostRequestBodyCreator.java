package com.e_commerce.users.util;

import com.e_commerce.users.dtos.AddressRecordCreateDto;
import com.e_commerce.users.dtos.UserRecordCreateDto;
import org.mockito.ArgumentMatchers;

public class UserPostRequestBodyCreator {
    public static UserRecordCreateDto create() {
        return UserRecordCreateDto.builder()
                .name(ArgumentMatchers.anyString())
                .email(ArgumentMatchers.anyString())
                .password(ArgumentMatchers.anyString())
                .cpf(ArgumentMatchers.anyString())
                .phone(ArgumentMatchers.anyString())
                .address(AddressRecordCreateDto.builder()
                        .street(ArgumentMatchers.anyString())
                        .neighbourhood(ArgumentMatchers.anyString())
                        .city(ArgumentMatchers.anyString())
                        .state(ArgumentMatchers.anyString())
                        .zipCode(ArgumentMatchers.anyString())
                        .country(ArgumentMatchers.anyString())
                        .build()
                ).build();
    }
}
