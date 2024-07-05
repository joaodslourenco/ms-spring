package com.e_commerce.users.mappers;

import com.e_commerce.users.dtos.AddressRecordCreateDto;
import com.e_commerce.users.models.AddressModel;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    AddressModel toAddress(AddressRecordCreateDto addressRecordCreateDto);
}