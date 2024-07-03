package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.services.UserService;
import com.e_commerce.users.util.UserCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@Log4j2
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userServiceMock;

    @BeforeEach
    void setup() {
        BDDMockito.when(userServiceMock.save(UserCreator.userRecordCreateDto())).thenReturn(UserCreator.validUser());
    }


    @Test
    @DisplayName("Save returns success when user is created")
    void save() {
        UserRecordCreateDto userRecordCreateDto = UserCreator.userRecordCreateDto();

        ResponseEntity<UserModel> savedUserResponse = userController.save(userRecordCreateDto);

        log.info(savedUserResponse);
        Assertions.assertThat(savedUserResponse.getBody()).isNotNull();
        Assertions.assertThat(savedUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}