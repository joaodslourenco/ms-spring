package com.e_commerce.users.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.e_commerce.users.models.UserModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Log4j2
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserModel user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant expirationDate = generateExpirationDate();

            return JWT.create()
                    .withIssuer("users-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT verify = JWT.require(algorithm)
                    .withIssuer("users-api")
                    .build()
                    .verify(token);

            log.info("verify: {}", verify);

            var subject = verify.getSubject();

            log.info("subject: {}", subject);

            return verify
                    .getSubject();
        } catch (JWTVerificationException exception) {
            log.info("exception braba", exception);
            return "";
        }
    }


    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}