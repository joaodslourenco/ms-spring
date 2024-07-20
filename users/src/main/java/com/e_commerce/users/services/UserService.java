package com.e_commerce.users.services;

import com.e_commerce.users.dtos.AddressCreateReqDto;
import com.e_commerce.users.dtos.AddressUpdateReqDto;
import com.e_commerce.users.dtos.UserCreateReqDto;
import com.e_commerce.users.dtos.UserRecordUpdateReqDto;
import com.e_commerce.users.exceptions.BadRequestException;
import com.e_commerce.users.mappers.AddressMapper;
import com.e_commerce.users.mappers.UserMapper;
import com.e_commerce.users.models.AddressModel;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.repositories.AddressRepository;
import com.e_commerce.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AuthService authService;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
//

    public UserModel save(UserCreateReqDto userCreateReqDto) {
        if (this.authService.loadUserByUsername(userCreateReqDto.email()) != null) {
            throw new BadRequestException("User already exists.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userCreateReqDto.password());

        UserModel newUser = UserModel.builder()
                .name(userCreateReqDto.name())
                .email(userCreateReqDto.email())
                .password(encryptedPassword)
                .role(userCreateReqDto.role())
                .build();

        return this.userRepository.save(newUser);
    }

    public UserModel findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found."));
    }

    public void delete(UUID id) {
        UserModel user = this.findById(id);

        userRepository.delete(user);
    }

    public UserModel update(UUID id, UserRecordUpdateReqDto userRecordUpdateReqDto) {
        UserModel savedUser = this.findById(id);

        userMapper.updateUserFromDto(userRecordUpdateReqDto, savedUser);

        return userRepository.save(savedUser);
    }

    @Transactional
    public UserModel createUserAddress(UUID userId, AddressCreateReqDto addressCreateReqDto) {
        UserModel savedUser = this.findById(userId);

        if (savedUser.getAddress() != null) throw new BadRequestException("User already has an address.");

        var newAddress = addressMapper.toAddress(addressCreateReqDto);

        newAddress.setUser(savedUser);
        savedUser.setAddress(newAddress);

        addressRepository.save(newAddress);
        return userRepository.save(savedUser);
    }

    @Transactional
    public UserModel updateUserAddress(UUID userId, AddressUpdateReqDto addressUpdateReqDto) {
        UserModel savedUser = this.findById(userId);
        AddressModel existingAddress = addressRepository.findById(savedUser.getAddress().getId()).orElseThrow(() -> new BadRequestException("User does not have an address."));

        addressMapper.updateAddressFromDto(addressUpdateReqDto, existingAddress);

        existingAddress.setUser(savedUser);
        savedUser.setAddress(existingAddress);

        addressRepository.save(existingAddress);
        return userRepository.save(savedUser);
    }


}
