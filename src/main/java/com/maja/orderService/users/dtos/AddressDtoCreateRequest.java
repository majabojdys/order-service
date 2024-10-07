package com.maja.orderService.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressDtoCreateRequest(@NotBlank @Size(max = 255) String street,
                                      @NotBlank @Size(max = 255) String number,
                                      @NotBlank @Size(max = 255) String city,
                                      @NotBlank @Size(max = 255) String zipCode,
                                      @NotBlank @Size(max = 255) String country) {
}
