package com.e_commerce.users.util;

import com.e_commerce.users.dtos.LoginReqDto;
import com.e_commerce.users.dtos.UserCreateReqDto;
import com.e_commerce.users.dtos.UserUpdateReqDto;
import com.e_commerce.users.enums.ERole;
import com.e_commerce.users.models.UserModel;

import java.util.UUID;

public class UserCreator {

    public static final UserCreateReqDto commonUserCreateReqDto = UserCreateReqDto.builder()
            .name("Test")
            .role(ERole.USER)
            .email("user@test.com")
            .password("safepassword")
            .build();


    public static final LoginReqDto commonUserLoginReqDto = LoginReqDto.builder()
            .email("user@test.com")
            .password("safepassword")
            .build();

    public static final UserCreateReqDto adminUserCreateReqDto = UserCreateReqDto.builder()
            .name("Test")
            .role(ERole.ADMIN)
            .email("admin@test.com")
            .password("safepassword")
            .build();

    public static final LoginReqDto adminUserLoginReqDto = LoginReqDto.builder()
            .email("admin@test.com")
            .password("safepassword")
            .build();

    public static final UserUpdateReqDto userUpdateReqDto = UserUpdateReqDto.builder()
            .name("João Editado")
            .email("emaileditad@teste.com")
            .build();


    public static final UserModel validUser = UserModel.builder()
            .id(UUID.fromString("551ac484-7eac-45c2-b163-5f7bdb8c90f1"))
            .name("João Silva")
            .email("joaosilva@exemplo.com")
            .password("senhaSegura")
            .build();


    public static UserModel validUserWithAddress = UserModel.builder()
            .id(UUID.fromString("551ac484-7eac-45c2-b163-5f7bdb8c90f1"))
            .name("João Silva")
            .email("joaosilva@exemplo.com")
            .password("senhaSegura")
            .address(AddressCreator.validAddress)
            .build();


}
