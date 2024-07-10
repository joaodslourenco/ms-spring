package com.e_commerce.users.services;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.dtos.UserRecordUpdateDto;
import com.e_commerce.users.exceptions.BadRequestException;
import com.e_commerce.users.mappers.AddressMapper;
import com.e_commerce.users.mappers.UserMapper;
import com.e_commerce.users.models.AddressModel;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;


    @Transactional
    public UserModel save(UserRecordCreateDto userRecordCreateDto) {
        UserModel existingUser = findByEmailOrCpf(userRecordCreateDto);

        if (existingUser != null) {
            throw new BadRequestException("User already exists.");
        }

        UserModel newUser = userMapper.toUserModel(userRecordCreateDto);
        AddressModel address = addressMapper.toAddress(userRecordCreateDto.address());

        newUser.setAddress(address);
        address.setUser(newUser);

        return userRepository.save(newUser);
    }

    public UserModel findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found."));
    }

    public void delete(UUID id) {
        UserModel user = this.findById(id);

        userRepository.delete(user);
    }

    public UserModel update(UUID id, UserRecordUpdateDto userRecordUpdateDto) {
        UserModel savedUser = this.findById(id);

        userMapper.updateUserFromDto(userRecordUpdateDto, savedUser);

        return userRepository.save(savedUser);
    }

    private UserModel findByEmailOrCpf(UserRecordCreateDto userRecordCreateDto) {
        return userRepository.findByEmailOrCpf(userRecordCreateDto.email(), userRecordCreateDto.cpf());
    }


}
