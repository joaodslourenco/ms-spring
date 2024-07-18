package com.e_commerce.users.util;

import com.e_commerce.users.dtos.AuthenticationLoginDto;
import com.e_commerce.users.dtos.LoginResponseDto;
import com.e_commerce.users.dtos.UserCreateResponseDto;
import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

public class AuthTestUtil {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private final UserRecordCreateDto commonUserRecordCreateDto = UserRecordCreateDto.builder()
            .name("Test")
            .role(ERole.USER)
            .email("user@test.com")
            .password("safepassword")
            .build();

    private final AuthenticationLoginDto commonUserLoginDto = AuthenticationLoginDto.builder()
            .email("user@test.com")
            .password("safepassword")
            .build();

    private final UserRecordCreateDto adminUserRecordCreateDto = UserRecordCreateDto.builder()
            .name("Test")
            .role(ERole.ADMIN)
            .email("admin@test.com")
            .password("safepassword")
            .build();

    private final AuthenticationLoginDto adminUserLoginDto = AuthenticationLoginDto.builder()
            .email("admin@test.com")
            .password("safepassword")
            .build();

    private void registerTestUser(ERole role) {
        UserRecordCreateDto userRecordCreateDto = role.equals(ERole.ADMIN) ? adminUserRecordCreateDto : commonUserRecordCreateDto;
        testRestTemplate.postForEntity("/users", userRecordCreateDto, UserCreateResponseDto.class);
    }

    private String loginAndGetToken(ERole role) {
        AuthenticationLoginDto authenticationLoginDto = role.equals(ERole.ADMIN) ? adminUserLoginDto : commonUserLoginDto;
        ResponseEntity<LoginResponseDto> loginResponse = testRestTemplate.postForEntity("/auth/login", authenticationLoginDto, LoginResponseDto.class);

        if (loginResponse.getBody() == null) return null;

        return loginResponse.getBody().accessToken();
    }


    public String registerAndLoginUser(ERole role) {
        this.registerTestUser(role);
        return this.loginAndGetToken(role);
    }
}
