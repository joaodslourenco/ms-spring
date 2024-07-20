package com.e_commerce.users.integration;

import com.e_commerce.users.dtos.LoginReqDto;
import com.e_commerce.users.dtos.LoginResDto;
import com.e_commerce.users.dtos.RefreshTokenReqDto;
import com.e_commerce.users.util.AuthTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import static com.e_commerce.users.enums.ERole.USER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AuthTestUtil authTestUtil;

    @LocalServerPort
    private int port;


    @TestConfiguration
    static class TestConfig {
        @Bean
        public AuthTestUtil authTestUtil() {
            return new AuthTestUtil();
        }
    }


    @Test
    @DisplayName("Login returns access and refresh tokens when successful")
    void login_ReturnsAccessAndRefreshToken_WhenSuccessful() {
        authTestUtil.registerTestUser(USER);

        LoginReqDto commonUserLoginDto = LoginReqDto.builder()
                .email("user@test.com")
                .password("safepassword")
                .build();

        ResponseEntity<LoginResDto> loginResponse = testRestTemplate.postForEntity("/auth/login", commonUserLoginDto, LoginResDto.class);

        Assertions.assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(loginResponse.getBody()).isNotNull();
        Assertions.assertThat(loginResponse.getBody().accessToken()).isNotEmpty();
        Assertions.assertThat(loginResponse.getBody().refreshToken()).isNotEmpty();
    }

    @Test
    @DisplayName("Login returns forbidden when user is not registered")
    void login_ReturnsForbidden_WhenUserIsNotRegistered() {
        LoginReqDto commonUserLoginDto = LoginReqDto.builder()
                .email("usernotregistered@test.com")
                .password("safepassword")
                .build();

        ResponseEntity<LoginResDto> loginResponse = testRestTemplate.postForEntity("/auth/login", commonUserLoginDto, LoginResDto.class);

        Assertions.assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Assertions.assertThat(loginResponse.getBody()).isNull();
    }

    @Test
    @DisplayName("Refresh returns access and refresh tokens when successful")
    void refresh_ReturnsAccessAndRefreshTokens_WhenAValidRefreshTokenIsSubmitted() {
        authTestUtil.registerTestUser(USER);

        LoginReqDto commonUserLoginDto = LoginReqDto.builder()
                .email("user@test.com")
                .password("safepassword")
                .build();


        ResponseEntity<LoginResDto> loginResponse = testRestTemplate.postForEntity("/auth/login", commonUserLoginDto, LoginResDto.class);
        var loginBody = loginResponse.getBody();

        if (loginBody == null) {
            Assertions.fail("Login response body is null");
            return;
        }

        RefreshTokenReqDto refreshTokenReqDto = RefreshTokenReqDto.builder().refreshToken(loginBody.refreshToken()).build();

        ResponseEntity<LoginResDto> refreshResponse = testRestTemplate.postForEntity("/auth/refresh", refreshTokenReqDto, LoginResDto.class);

        Assertions.assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(refreshResponse.getBody()).isNotNull();
        Assertions.assertThat(refreshResponse.getBody().accessToken()).isNotEmpty();
        Assertions.assertThat(refreshResponse.getBody().refreshToken()).isNotEmpty();
    }

}
