package com.e_commerce.users.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
class RestrictedControllerTest {
    @InjectMocks
    private RestrictedController restrictedController;

    @Test
    @DisplayName("Ping returns admin pong")
    void ping() {
        ResponseEntity<String> response = restrictedController.ping();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("admin pong");

    }
}