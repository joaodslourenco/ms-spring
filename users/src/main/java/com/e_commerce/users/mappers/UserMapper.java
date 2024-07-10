package com.e_commerce.users.mappers;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.dtos.UserRecordUpdateDto;
import com.e_commerce.users.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserModel toUserModel(UserRecordCreateDto userRecordCreateDto);

    void updateUserFromDto(UserRecordUpdateDto userRecordUpdateDto, @MappingTarget UserModel userModel);

}
