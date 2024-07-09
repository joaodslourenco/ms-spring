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
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
}