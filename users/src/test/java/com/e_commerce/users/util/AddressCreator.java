package com.e_commerce.users.util;

import com.e_commerce.users.dtos.AddressRecordCreateDto;
import com.e_commerce.users.dtos.AddressRecordUpdateDto;
import com.e_commerce.users.models.AddressModel;

import java.util.UUID;

public class AddressCreator {

    public static AddressRecordCreateDto addressRecordCreateDto() {
        return AddressRecordCreateDto.builder()
                .street("Rua 1")
                .neighbourhood("Bairro")
                .city("Cidade")
                .state("Estado")
                .country("Brazuca")
                .zipCode("12345567")
                .build();
    }

    public static AddressRecordUpdateDto addressRecordUpdateDto() {
        return AddressRecordUpdateDto.builder()
                .city("Cidade editada")
                .state("Estado editado")
                .build();
    }

    public static AddressModel validAddress() {
        return AddressModel.builder()
                .id(UUID.fromString("3f4a8a37-7dc4-4e15-b0d3-3a21e2f8ab63"))
                .street("Rua 1")
                .neighbourhood("Bairro")
                .city("Cidade")
                .state("Estado")
                .country("Brazuca")
                .zipCode("12345567")
                .build();
    }
}
