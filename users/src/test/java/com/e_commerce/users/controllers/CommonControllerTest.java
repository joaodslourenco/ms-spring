package com.e_commerce.users.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
class CommonControllerTest {
    @InjectMocks
    private CommonController commonController;

    @Test
    @DisplayName("Ping returns user pong")
    void ping() {
        ResponseEntity<String> response = commonController.ping();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo("user pong");

    }
}