package com.e_commerce.users.services;

import com.e_commerce.users.enums.ETokenType;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepositoryMock;

    private UserModel validUserModel;

    private void setPrivateField(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        validUserModel = new UserModel();
        validUserModel.setEmail("user@example.com");

        Mockito.when(userRepositoryMock.findByEmail("user@example.com")).thenReturn(validUserModel);

        setPrivateField(authService, "accessTokenSecret", "testAccessTokenSecret");
        setPrivateField(authService, "refreshTokenSecret", "testRefreshTokenSecret");

    }

    @Test
    @DisplayName("loadUserByUsername returns user details when user exists")
    void loadUserByUsernameReturnsUserDetailsWhenUserExists() {
        UserDetails userDetails = authService.loadUserByUsername("user@example.com");

        assertNotNull(userDetails);
        assertEquals("user@example.com", userDetails.getUsername());
    }

    @Test
    @DisplayName("loadUserByUsername throws UsernameNotFoundException when user does not exist")
    void loadUserByUsernameThrowsUsernameNotFoundExceptionWhenUserDoesNotExist() {
        Mockito.when(userRepositoryMock.findByEmail("nonexistent@example.com")).thenReturn(null);

        Mockito.doThrow(UsernameNotFoundException.class).when(userRepositoryMock).findByEmail("nonexistent@example.com");

        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    @DisplayName("generateToken returns a non-empty access token for valid user")
    void generateTokenReturnsANonEmptyAccessTokenForValidUser() {
        String accessToken = authService.generateToken(validUserModel, ETokenType.ACCESS);

        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
    }

    @Test
    @DisplayName("generateToken returns a non-empty refresh token for valid user")
    void generateTokenReturnsANonEmptyRefreshTokenForValidUser() {
        String refreshToken = authService.generateToken(validUserModel, ETokenType.REFRESH);

        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
    }

    @Test
    @DisplayName("recoverToken extracts token from Authorization header")
    void recoverTokenExtractsTokenFromAuthorizationHeader() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        String token = authService.recoverToken(request);

        assertEquals("validToken", token);
    }

    @Test
    @DisplayName("recoverToken returns null when Authorization header is missing")
    void recoverTokenReturnsNullWhenAuthorizationHeaderIsMissing() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);

        String token = authService.recoverToken(request);

        assertNull(token);
    }
}