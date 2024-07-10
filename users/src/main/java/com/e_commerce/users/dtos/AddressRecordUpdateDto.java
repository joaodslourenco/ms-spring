package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record AddressRecordUpdateDto(String street, String neighbourhood, String city, String state, String zipCode,
                                     String country) {
}
