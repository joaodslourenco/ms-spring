package com.e_commerce.users.mappers;

import com.e_commerce.users.dtos.AddressCreateReqDto;
import com.e_commerce.users.dtos.AddressUpdateReqDto;
import com.e_commerce.users.models.AddressModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    AddressModel toAddress(AddressCreateReqDto addressCreateReqDto);

    void updateAddressFromDto(AddressUpdateReqDto addressUpdateReqDto, @MappingTarget AddressModel addressModel);
}
