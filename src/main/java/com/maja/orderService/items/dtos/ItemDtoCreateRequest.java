package com.maja.orderService.items.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ItemDtoCreateRequest(@NotBlank @Size(max = 255) String name,
                                   @NotNull @DecimalMin("0.01") BigDecimal price,
                                   @NotBlank @Size(max = 255) String color) {
}
