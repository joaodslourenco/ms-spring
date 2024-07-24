package com.e_commerce.users.mappers;

import com.e_commerce.users.dtos.UserCreateReqDto;
import com.e_commerce.users.dtos.UserUpdateReqDto;
import com.e_commerce.users.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserModel toUserModel(UserCreateReqDto userCreateReqDto);

    void updateUserFromDto(UserUpdateReqDto userUpdateReqDto, @MappingTarget UserModel userModel);

}
