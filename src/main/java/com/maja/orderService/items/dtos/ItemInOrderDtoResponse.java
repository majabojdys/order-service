package com.maja.orderService.items.dtos;

import java.math.BigDecimal;

public record ItemInOrderDtoResponse(String name, BigDecimal price, String color, Integer quantity) {
}
