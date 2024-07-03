package com.e_commerce.users.services;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.mappers.UserMapper;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.repositories.UserRepository;
import com.e_commerce.users.util.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        BDDMockito.when(userRepositoryMock.save(UserCreator.validUser())).thenReturn(UserCreator.validUser());
        BDDMockito.when(userMapper.toUserModel(UserCreator.userRecordCreateDto())).thenReturn(UserCreator.validUser());
    }

    @Test
    @DisplayName("Save returns user when successful")
    void save() {
        UserRecordCreateDto newUser = UserCreator.userRecordCreateDto();

        UserModel savedUser = userService.save(newUser);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser).isEqualTo(UserCreator.validUser());
    }
}