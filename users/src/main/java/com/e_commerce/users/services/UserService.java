package com.e_commerce.users.services;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.mappers.UserMapper;
import com.e_commerce.users.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;


    public UserModel save(UserRecordCreateDto userRecordCreateDto) {
        UserModel newUser = userMapper.toUserModel(userRecordCreateDto);

        return newUser;
    }
}
