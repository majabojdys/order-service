package com.maja.orderService.items.dtos;

import java.math.BigDecimal;

public record ItemDtoResponse(Long id, String name, String price, String color) {
}
