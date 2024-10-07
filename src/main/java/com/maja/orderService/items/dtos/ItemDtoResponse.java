package com.maja.orderService.items.dtos;

import java.math.BigDecimal;

public record ItemDtoResponse(String name, BigDecimal price, String color, Integer quantity) {
}
