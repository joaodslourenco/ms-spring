package com.e_commerce.users.services;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.mappers.AddressMapper;
import com.e_commerce.users.mappers.UserMapper;
import com.e_commerce.users.models.AddressModel;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;


    @Transactional
    public UserModel save(UserRecordCreateDto userRecordCreateDto) {
        UserModel newUser = userMapper.toUserModel(userRecordCreateDto);
        AddressModel address = addressMapper.toAddress(userRecordCreateDto.address());

        newUser.setAddress(address);
        address.setUser(newUser);

        return userRepository.save(newUser);
    }
}
