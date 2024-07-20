package com.e_commerce.users.util;

import com.e_commerce.users.dtos.LoginReqDto;
import com.e_commerce.users.dtos.LoginResDto;
import com.e_commerce.users.dtos.UserCreateReqDto;
import com.e_commerce.users.dtos.UserCreateResDto;
import com.e_commerce.users.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

public class AuthTestUtil {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private final UserCreateReqDto commonUserCreateReqDto = UserCreateReqDto.builder()
            .name("Test")
            .role(ERole.USER)
            .email("user@test.com")
            .password("safepassword")
            .build();

    private final LoginReqDto commonUserLoginDto = LoginReqDto.builder()
            .email("user@test.com")
            .password("safepassword")
            .build();

    private final UserCreateReqDto adminUserCreateReqDto = UserCreateReqDto.builder()
            .name("Test")
            .role(ERole.ADMIN)
            .email("admin@test.com")
            .password("safepassword")
            .build();

    private final LoginReqDto adminUserLoginDto = LoginReqDto.builder()
            .email("admin@test.com")
            .password("safepassword")
            .build();

    public void registerTestUser(ERole role) {
        UserCreateReqDto userCreateReqDto = role.equals(ERole.ADMIN) ? adminUserCreateReqDto : commonUserCreateReqDto;
        testRestTemplate.postForEntity("/users", userCreateReqDto, UserCreateResDto.class);
    }

    public String loginAndGetToken(ERole role) {
        LoginReqDto loginReqDto = role.equals(ERole.ADMIN) ? adminUserLoginDto : commonUserLoginDto;
        ResponseEntity<LoginResDto> loginResponse = testRestTemplate.postForEntity("/auth/login", loginReqDto, LoginResDto.class);

        if (loginResponse.getBody() == null) return null;

        return loginResponse.getBody().accessToken();
    }


    public String registerAndLoginUser(ERole role) {
        this.registerTestUser(role);
        return this.loginAndGetToken(role);
    }
}
