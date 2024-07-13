package com.e_commerce.users.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.e_commerce.users.enums.ETokenType;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Value("${api.security.access-token.secret}")
    private String accessTokenSecret;

    @Value("${api.security.access-token.expiration}")
    private long accessTokenExpirationHours;

    @Value("${api.security.refresh-token.secret}")
    private String refreshTokenSecret;

    @Value("${api.security.refresh-token.expiration}")
    private long refreshTokenExpirationHours;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public String validateToken(String token, ETokenType tokenType) {
        try {
            Algorithm algorithm = getAlgorithm(tokenType);

            return JWT.require(algorithm)
                    .withIssuer("users-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    public String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        return authHeader.replace("Bearer ", "");
    }

    public String generateToken(UserModel user, ETokenType tokenType) {
        try {
            Algorithm algorithm = getAlgorithm(tokenType);
            Instant expirationDate = generateExpirationDate(tokenType);

            return JWT.create()
                    .withIssuer("users-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    private Instant generateExpirationDate(ETokenType tokenType) {
        var expirationHours = tokenType == ETokenType.ACCESS ? accessTokenExpirationHours : refreshTokenExpirationHours;

        return LocalDateTime.now().plusHours(expirationHours).toInstant(ZoneOffset.of("-03:00"));
    }

    private Algorithm getAlgorithm(ETokenType tokenType) {
        var secret = tokenType == ETokenType.ACCESS ? accessTokenSecret : refreshTokenSecret;

        return Algorithm.HMAC256(secret);
    }

}
