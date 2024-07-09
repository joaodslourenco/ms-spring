package com.e_commerce.users.services;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.exceptions.BadRequestException;
import com.e_commerce.users.mappers.AddressMapper;
import com.e_commerce.users.mappers.UserMapper;
import com.e_commerce.users.models.UserModel;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@Log4j2
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressMapper addressMapper;

    @BeforeEach
    void setup() {
        BDDMockito.when(userMapper.toUserModel(UserCreator.userRecordCreateDto())).thenReturn(UserCreator.validUser());
        BDDMockito.when(addressMapper.toAddress(AddressCreator.addressRecordCreateDto())).thenReturn(AddressCreator.validAddress());
        BDDMockito.when(userRepositoryMock.save(UserCreator.validUser())).thenReturn(UserCreator.validUser());
        BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.empty());
        BDDMockito.when(userRepositoryMock.findById(UserCreator.validUser().getId())).thenReturn(Optional.ofNullable(UserCreator.validUser()));
    }

    @Test
    @DisplayName("Save returns user when successful")
    void save() {
        UserRecordCreateDto newUser = UserCreator.userRecordCreateDto();

        UserModel savedUser = userService.save(newUser);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser).isEqualTo(UserCreator.validUser());
    }

    @Test
    @DisplayName("Save throws BadRequestException if an user with same email or cpf already exists")
    void save_ThrowsBadRequest_WhenUserWithSameEmailOrCpfAlreadyExists() {
        UserRecordCreateDto newUser = UserCreator.userRecordCreateDto();
        UserModel savedUser = userService.save(newUser);

        BDDMockito.when(userRepositoryMock.findByEmailOrCpf(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(savedUser);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.save(newUser))
                .withMessageContaining("User already exists.");
    }

    @Test
    @DisplayName("findById retrieves user")
    void findById_RetrievesUser_WhenSuccessful() {
        UUID expectedId = UserCreator.validUser().getId();

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
        UserModel user = UserCreator.validUser();

        BDDMockito.when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.of(user));

        Assertions.assertThatCode(() -> userService.delete(user.getId())).doesNotThrowAnyException();

        BDDMockito.verify(userRepositoryMock, Mockito.times(1)).delete(user);

    }


    @Test
    @DisplayName("delete throws Bad Request when no user was found")
    void delete_ThrowsBadRequest_WhenNoUserWasFound() {
        UserModel user = UserCreator.validUser();

        BDDMockito.doThrow(new BadRequestException("User not found")).when(userRepositoryMock).delete(user);

        Assertions.assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> userService.delete(user.getId()));
    }
}