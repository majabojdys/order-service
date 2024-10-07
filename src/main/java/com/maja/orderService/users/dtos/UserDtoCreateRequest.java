package com.maja.orderService.users.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDtoCreateRequest(@NotBlank @Size(max = 255) String firstName,
                                   @NotBlank @Size(max = 255) String lastName,
                                   @NotBlank @Email @Size(max = 255) String email,
                                   @NotBlank @Size(min = 5, max = 255) String password,
                                   @NotNull @Valid AddressDtoCreateRequest address) {
}
