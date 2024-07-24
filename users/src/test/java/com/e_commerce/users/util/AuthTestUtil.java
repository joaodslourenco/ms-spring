package com.e_commerce.users.util;

import com.e_commerce.users.dtos.LoginReqDto;
import com.e_commerce.users.dtos.LoginResDto;
import com.e_commerce.users.dtos.UserCreateReqDto;
import com.e_commerce.users.dtos.UserCreateResDto;
import com.e_commerce.users.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class AuthTestUtil {
    @Autowired
    private TestRestTemplate testRestTemplate;

    public void registerTestUser(ERole role) {
        UserCreateReqDto userCreateReqDto = role.equals(ERole.ADMIN) ? UserCreator.adminUserCreateReqDto : UserCreator.commonUserCreateReqDto;
        testRestTemplate.postForEntity("/users", userCreateReqDto, UserCreateResDto.class);
    }

    public String loginAndGetToken(ERole role) {
        LoginReqDto loginReqDto = role.equals(ERole.ADMIN) ? UserCreator.adminUserLoginReqDto : UserCreator.commonUserLoginReqDto;
        ResponseEntity<LoginResDto> loginResponse = testRestTemplate.postForEntity("/auth/login", loginReqDto, LoginResDto.class);

        if (loginResponse.getBody() == null) return null;

        return loginResponse.getBody().accessToken();
    }


    public String registerAndLoginUser(ERole role) {
        this.registerTestUser(role);
        return this.loginAndGetToken(role);
    }

    public HttpHeaders getHeaders(String token, boolean isJson) {
        HttpHeaders headers = new HttpHeaders();
        if (isJson) headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }


}
