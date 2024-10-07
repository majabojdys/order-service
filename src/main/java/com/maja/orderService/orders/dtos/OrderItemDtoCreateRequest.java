package com.maja.orderService.orders.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemDtoCreateRequest(@NotNull Long id,
                                        @NotNull @Min(1) Integer quantity) {
}