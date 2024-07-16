package com.e_commerce.users.util;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.dtos.UserRecordUpdateDto;
import com.e_commerce.users.models.UserModel;

import java.util.UUID;

public class UserCreator {
    public static UserRecordCreateDto userRecordCreateDto() {
        return UserRecordCreateDto.builder()
                .name("João Silva")
                .email("joaosilva@exemplo.com")
                .password("senhaSegura")
                .build();
    }

    public static UserRecordUpdateDto userRecordUpdateDto() {
        return UserRecordUpdateDto.builder()
                .name("João Editado")
                .email("emaileditad@teste.com")
                .build();
    }

    public static UserModel validUser() {
        return UserModel.builder()
                .id(UUID.fromString("551ac484-7eac-45c2-b163-5f7bdb8c90f1"))
                .name("João Silva")
                .email("joaosilva@exemplo.com")
                .password("senhaSegura")
                .build();
    }
    public static UserModel validUserWithAddress() {
        return UserModel.builder()
                .id(UUID.fromString("551ac484-7eac-45c2-b163-5f7bdb8c90f1"))
                .name("João Silva")
                .email("joaosilva@exemplo.com")
                .password("senhaSegura")
                .address(AddressCreator.validAddress())
                .build();
    }

}
