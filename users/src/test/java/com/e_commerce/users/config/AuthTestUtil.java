package com.e_commerce.users.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AuthTestUtil {
    @Bean
    public com.e_commerce.users.util.AuthTestUtil authTestUtil() {
        return new com.e_commerce.users.util.AuthTestUtil();
    }
}
