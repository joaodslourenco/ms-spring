package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record AddressUpdateReqDto(String street, String neighbourhood, String city, String state, String zipCode,
                                  String country) {
}
