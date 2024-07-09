package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.exceptions.BadRequestException;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.services.UserService;
import com.e_commerce.users.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userServiceMock;

    @BeforeEach
    void setup() {
        BDDMockito.when(userServiceMock.save(UserCreator.userRecordCreateDto())).thenReturn(UserCreator.validUser());
        BDDMockito.when(userServiceMock.findById(ArgumentMatchers.any(UUID.class))).thenReturn(UserCreator.validUser());
    }

    @Test
    @DisplayName("Save creates user when successful")
    void save_CreatesUser_WhenSuccessful() {
        UserRecordCreateDto userRecordCreateDto = UserCreator.userRecordCreateDto();

        ResponseEntity<UserModel> savedUserResponse = userController.save(userRecordCreateDto);

        Assertions.assertThat(savedUserResponse.getBody()).isNotNull();
        Assertions.assertThat(savedUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }


    @Test
    @DisplayName("findById retrieves user")
    void findById_RetrievesUser_WhenSuccessful() {
        UUID expectedId = UserCreator.validUser().getId();

        ResponseEntity<UserModel> user = userController.findById(expectedId);


        Assertions.assertThat(user.getBody()).isNotNull();
        Assertions.assertThat(user.getBody().getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById throws Bad Request when no user was found with the requested ID")
    void findById_ThrowsBadRequest_WhenNoUserWasFound() {
        UUID invalidId = UUID.randomUUID();

        BDDMockito.when(userServiceMock.findById(invalidId)).thenThrow(new BadRequestException("User not found"));

        Assertions.assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> userController.findById(invalidId));
    }


}