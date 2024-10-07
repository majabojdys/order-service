package com.maja.orderService.orders.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderDtoCreateRequest(@NotEmpty @Valid List<OrderItemDtoCreateRequest> orderedItems) {
}
