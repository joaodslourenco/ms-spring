package com.e_commerce.users.services;

import com.e_commerce.users.dtos.UserRecordCreateDto;
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
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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