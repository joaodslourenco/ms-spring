package com.e_commerce.users.util;

import com.e_commerce.users.dtos.AddressRecordCreateDto;
import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.models.AddressModel;
import com.e_commerce.users.models.UserModel;
import org.mockito.ArgumentMatchers;
import org.springframework.security.core.userdetails.User;

import java.util.UUID;

public class UserCreator {
    public static UserRecordCreateDto userRecordCreateDto() {
        return UserRecordCreateDto.builder()
                .name("João Silva")
                .email("joaosilva@exemplo.com")
                .password("senhaSegura")
                .cpf("123.456.789-00")
                .phone("+55-11-98765-4321")
                .address(AddressRecordCreateDto.builder()
                        .street("Rua Principal, 123")
                        .neighbourhood("Centro")
                        .city("São Paulo")
                        .state("SP")
                        .zipCode("01001-000")
                        .country("Brasil")
                        .build()
                ).build();
    }

    public static UserModel validUser() {
        return UserModel.builder()
                .id(UUID.fromString("551ac484-7eac-45c2-b163-5f7bdb8c90f1"))
                .name("João Silva")
                .email("joaosilva@exemplo.com")
                .password("senhaSegura")
                .cpf("123.456.789-00")
                .phone("+55-11-98765-4321")
                .address(AddressModel.builder()
                        .id(UUID.fromString("ffa67750-2493-42f7-b3be-f42fc3a1d758"))
                        .street("Rua Principal, 123")
                        .neighbourhood("Centro")
                        .city("São Paulo")
                        .state("SP")
                        .zipCode("01001-000")
                        .country("Brasil")
                        .build()
                ).build();
    }
}
