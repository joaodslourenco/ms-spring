package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.LoginReqDto;
import com.e_commerce.users.dtos.LoginResDto;
import com.e_commerce.users.dtos.RefreshTokenReqDto;
import com.e_commerce.users.enums.ETokenType;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.services.AuthService;
import com.e_commerce.users.util.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authServiceMock;

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Test
    @DisplayName("login returns token response when credentials are valid")
    void login_ReturnsTokenResponse_WhenCredentialsAreValid() {
        LoginReqDto authDto = LoginReqDto.builder().email("email@email.com").password("safepassword").build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
        Authentication auth = mock(Authentication.class);
        UserModel userModel = mock(UserModel.class);
        when(authenticationManagerMock.authenticate(usernamePasswordAuthenticationToken)).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userModel);
        when(authServiceMock.generateToken(userModel, ETokenType.ACCESS)).thenReturn("access-token");
        when(authServiceMock.generateToken(userModel, ETokenType.REFRESH)).thenReturn("refresh-token");

        ResponseEntity<LoginResDto> response = authController.login(authDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("access-token", response.getBody().accessToken());
        assertEquals("refresh-token", response.getBody().refreshToken());
    }

    @Test
    @DisplayName("login throws exception when authentication fails")
    void login_ThrowsException_WhenAuthenticationFails() {
        LoginReqDto authDto = LoginReqDto.builder().email("email@email.com").password("safepassword").build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
        when(authenticationManagerMock.authenticate(usernamePasswordAuthenticationToken)).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authController.login(authDto));
    }

    @Test
    @DisplayName("refreshToken updates tokens for valid refresh token")
    void refreshToken_UpdatesTokens_ForValidRefreshToken() {
        RefreshTokenReqDto refreshTokenReqDto = RefreshTokenReqDto.builder().refreshToken("valid-refresh-token").build();
        Authentication auth = mock(Authentication.class);
        UserModel userModel = UserCreator.validUser;
        when(authServiceMock.validateToken(refreshTokenReqDto.refreshToken(), ETokenType.REFRESH)).thenReturn("user@example.com");
        when(authServiceMock.loadUserByUsername("user@example.com")).thenReturn(userModel);
        when(authServiceMock.generateToken(userModel, ETokenType.ACCESS)).thenReturn("updated-access-token");
        when(authServiceMock.generateToken(userModel, ETokenType.REFRESH)).thenReturn("updated-refresh-token");
        when(auth.getPrincipal()).thenReturn(userModel);

        ResponseEntity<LoginResDto> response = authController.refreshToken(refreshTokenReqDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("updated-access-token", response.getBody().accessToken());
        assertEquals("updated-refresh-token", response.getBody().refreshToken());
    }
}