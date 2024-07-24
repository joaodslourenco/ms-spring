package com.e_commerce.users.services;

import com.e_commerce.users.dtos.UserCreateReqDto;
import com.e_commerce.users.dtos.UserUpdateReqDto;
import com.e_commerce.users.exceptions.BadRequestException;
import com.e_commerce.users.mappers.AddressMapper;
import com.e_commerce.users.mappers.UserMapper;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.repositories.AddressRepository;
import com.e_commerce.users.repositories.UserRepository;
import com.e_commerce.users.util.AddressCreator;
import com.e_commerce.users.util.UserCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Log4j2
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private AddressRepository addressRepositoryMock;

    @Mock
    private AuthService authServiceMock;

    @Mock
    private UserMapper userMapperMock;

    @Mock
    private AddressMapper addressMapperMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @BeforeEach
    void setup() {
        BDDMockito.when(passwordEncoderMock.encode(ArgumentMatchers.anyString())).thenReturn(ArgumentMatchers.anyString());
        BDDMockito.when(userMapperMock.toUserModel(UserCreator.commonUserCreateReqDto)).thenReturn(UserCreator.validUser);
        BDDMockito.when(addressMapperMock.toAddress(AddressCreator.addressCreateReqDto)).thenReturn(AddressCreator.validAddress);
        BDDMockito.when(userRepositoryMock.save(ArgumentMatchers.any(UserModel.class))).thenReturn(UserCreator.validUser);
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.empty());
        BDDMockito.when(userRepositoryMock.findById(UserCreator.validUser.getId())).thenReturn(Optional.of(UserCreator.validUser));
    }

    @Test
    @DisplayName("Save returns user when successful")
    void save() {
        UserCreateReqDto newUser = UserCreator.commonUserCreateReqDto;

        UserModel savedUser = userService.save(newUser);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser).isEqualTo(UserCreator.validUser);
    }

    @Test
    @DisplayName("Save throws BadRequestException if an user with same email already exists")
    void save_ThrowsBadRequest_WhenUserWithSameEmailAlreadyExists() {
        UserCreateReqDto newUser = UserCreator.commonUserCreateReqDto;
        UserModel savedUser = userService.save(newUser);

        BDDMockito.when(authServiceMock.loadUserByUsername(ArgumentMatchers.anyString()))
                .thenReturn(savedUser);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.save(newUser))
                .withMessageContaining("User already exists.");
    }

    @Test
    @DisplayName("findById retrieves user")
    void findById_RetrievesUser_WhenSuccessful() {
        UUID expectedId = UserCreator.validUser.getId();

        UserModel user = userService.findById(expectedId);


        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById throws Bad Request when no user was found with the requested ID")
    void findById_ThrowsBadRequest_WhenNoUserWasFound() {
        UUID invalidId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.findById(invalidId))
                .withMessageContaining("User not found.");
    }

    @Test
    @DisplayName("delete removes user when successful")
    void delete_RemovesUser_WhenSuccessful() {
        UserModel user = UserCreator.validUser;

        BDDMockito.when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.of(user));

        Assertions.assertThatCode(() -> userService.delete(user.getId())).doesNotThrowAnyException();

        verify(userRepositoryMock, Mockito.times(1)).delete(user);
    }


    @Test
    @DisplayName("delete throws Bad Request when no user was found")
    void delete_ThrowsBadRequest_WhenNoUserWasFound() {
        UserModel user = UserCreator.validUser;

        BDDMockito.doThrow(new BadRequestException("User not found")).when(userRepositoryMock).delete(user);

        Assertions.assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> userService.delete(user.getId()));
    }

    @Test
    @DisplayName("update modifies user when successful")
    void update_ModifierUser_WhenSuccessful() {
        UserModel user = UserCreator.validUser;
        UUID userId = user.getId();

        UserUpdateReqDto userUpdateReqDto = UserCreator.userUpdateReqDto;
        BDDMockito.when(userRepositoryMock.save(ArgumentMatchers.any(UserModel.class))).thenReturn(UserModel.builder().build());

        Assertions.assertThatCode(() -> userService.update(userId, userUpdateReqDto)).doesNotThrowAnyException();

        verify(userRepositoryMock).findById(userId);
        verify(userMapperMock).updateUserFromDto(userUpdateReqDto, user);
        verify(userRepositoryMock).save(user);
    }

    @Test
    @DisplayName("update throws Bad Request when no user was found")
    void update_ThrowsBadRequest_WhenNoUserWasFound() {
        UUID expectedId = UserCreator.validUser.getId();

        BDDMockito.doThrow(new BadRequestException("User not found"))
                .when(userRepositoryMock).findById(expectedId);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.update(expectedId, UserCreator.userUpdateReqDto));
    }

    @Test
    @DisplayName("createUserAddress adds address to user when successful")
    void createUserAddress_AddsAddressToUser_WhenSuccessful() {
        var user = UserCreator.validUser;
        var address = AddressCreator.addressCreateReqDto;

        UserModel userWithAddress = userService.createUserAddress(user.getId(), address);

        Assertions.assertThat(userWithAddress).isNotNull();
    }

    @Test
    @DisplayName("updateUserAddress updates address of user when successful")
    void updateUserAddress_UpdatesAddressOfUser_WhenSuccessful() {
        var user = UserCreator.validUserWithAddress;
        var address = AddressCreator.addressUpdateReqDto;

        BDDMockito.when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.ofNullable(UserCreator.validUserWithAddress));
        BDDMockito.when(addressRepositoryMock.findById(user.getAddress().getId())).thenReturn(Optional.of(AddressCreator.validAddress));

        UserModel userWithAddress = userService.updateUserAddress(user.getId(), address);

        Assertions.assertThatCode(() -> userService.updateUserAddress(user.getId(), address)).doesNotThrowAnyException();
        Assertions.assertThat(userWithAddress).isNotNull();
    }

}