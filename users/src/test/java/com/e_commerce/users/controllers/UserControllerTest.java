package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.AddressRecordCreateDto;
import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.util.UserPostRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Save returns success when user is created")
    void save() {
        UserRecordCreateDto userRecordCreateDto = UserPostRequestBodyCreator.create();

        ResponseEntity<String> savedUserResponse = userController.save(userRecordCreateDto);

        Assertions.assertThat(savedUserResponse.getBody()).isEqualTo("created");
        Assertions.assertThat(savedUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
}