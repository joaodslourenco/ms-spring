package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.UserCreateReqDto;
import com.e_commerce.users.dtos.UserUpdateReqDto;
import com.e_commerce.users.exceptions.BadRequestException;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.services.UserService;
import com.e_commerce.users.util.AddressCreator;
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
        BDDMockito.when(userServiceMock.save(UserCreator.commonUserCreateReqDto)).thenReturn(UserCreator.validUser);
        BDDMockito.when(userServiceMock.findById(ArgumentMatchers.any(UUID.class))).thenReturn(UserCreator.validUser);
        BDDMockito.when(userServiceMock.update(UserCreator.validUser.getId(), UserCreator.userUpdateReqDto)).thenReturn(UserCreator.validUser);
        BDDMockito.when(userServiceMock.createUserAddress(UserCreator.validUser.getId(), AddressCreator.addressCreateReqDto)).thenReturn(UserCreator.validUserWithAddress);
        BDDMockito.when(userServiceMock.updateUserAddress(UserCreator.validUser.getId(), AddressCreator.addressUpdateReqDto)).thenReturn(UserCreator.validUserWithAddress);
    }

    @Test
    @DisplayName("Save creates user when successful")
    void save_CreatesUser_WhenSuccessful() {
        UserCreateReqDto userCreateReqDto = UserCreator.commonUserCreateReqDto;

        ResponseEntity<UserModel> savedUserResponse = userController.save(userCreateReqDto);

        Assertions.assertThat(savedUserResponse.getBody()).isNotNull();
        Assertions.assertThat(savedUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Save throws BadRequestException if an user with same email or cpf already exists")
    void save_ThrowsBadRequest_WhenUserWithSameEmailOrCpfAlreadyExists() {
        UserCreateReqDto userCreateReqDto = UserCreator.commonUserCreateReqDto;

        BDDMockito.when(userServiceMock.save(ArgumentMatchers.any(UserCreateReqDto.class))).thenThrow(new BadRequestException("User already exists."));

        Assertions.assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> userController.save(userCreateReqDto));
    }


    @Test
    @DisplayName("findById retrieves user")
    void findById_RetrievesUser_WhenSuccessful() {
        UUID expectedId = UserCreator.validUser.getId();

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

    @Test
    @DisplayName("delete removes user when successful")
    void delete_RemovesUser_WhenSuccessful() {
        UUID expectedId = UserCreator.validUser.getId();

        Assertions.assertThatCode(() -> userController.delete(expectedId)).doesNotThrowAnyException();

        ResponseEntity<Void> entity = userController.delete(expectedId);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("delete throws Bad Request when no user was found")
    void delete_ThrowsBadRequest_WhenNoUserWasFound() {
        UUID expectedId = UserCreator.validUser.getId();

        BDDMockito.doThrow(new BadRequestException("User not found")).when(userServiceMock).delete(expectedId);

        Assertions.assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> userController.delete(expectedId));
    }

    @Test
    @DisplayName("update modifies user when successful")
    void update_ModifierUser_WhenSuccessful() {
        UserModel user = UserCreator.validUser;

        UserUpdateReqDto userUpdateReqDto = UserCreator.userUpdateReqDto;

        Assertions.assertThatCode(() -> userController.update(user.getId(), userUpdateReqDto)).doesNotThrowAnyException();

        ResponseEntity<UserModel> responseEntity = userController.update(user.getId(), userUpdateReqDto);

        UserModel responseBody = responseEntity.getBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("update throws Bad Request when no user was found")
    void update_ThrowsBadRequest_WhenNoUserWasFound() {
        UUID expectedId = UserCreator.validUser.getId();

        BDDMockito.doThrow(new BadRequestException("User not found"))
                .when(userServiceMock).update(expectedId, UserCreator.userUpdateReqDto);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userController.update(expectedId, UserCreator.userUpdateReqDto));
    }

    @Test
    @DisplayName("createUserAddress adds address to user when successful")
    void createUserAddress_AddsAddressToUser_WhenSuccessful() {
        var user = UserCreator.validUser;
        var address = AddressCreator.addressCreateReqDto;

        ResponseEntity<UserModel> userWithAddress = userController.createUserAddress(user.getId(), address);

        Assertions.assertThatCode(() -> userController.createUserAddress(user.getId(), address)).doesNotThrowAnyException();
        Assertions.assertThat(userWithAddress.getBody()).isNotNull();
        Assertions.assertThat(userWithAddress.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("updateUserAddress updates address of user when successful")
    void updateUserAddress_UpdatesAddressOfUser_WhenSuccessful() {
        var user = UserCreator.validUser;
        var address = AddressCreator.addressUpdateReqDto;

        ResponseEntity<UserModel> userWithAddress = userController.updateUserAddress(user.getId(), address);

        Assertions.assertThatCode(() -> userController.updateUserAddress(user.getId(), address)).doesNotThrowAnyException();
        Assertions.assertThat(userWithAddress.getBody()).isNotNull();
        Assertions.assertThat(userWithAddress.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}