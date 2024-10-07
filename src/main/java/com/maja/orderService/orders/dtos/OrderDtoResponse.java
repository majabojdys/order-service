package com.maja.orderService.orders.dtos;

import com.maja.orderService.items.dtos.ItemInOrderDtoResponse;
import com.maja.orderService.orders.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderDtoResponse(Long id, OrderStatus orderStatus, List<ItemInOrderDtoResponse> items, BigDecimal totalSum) {
}
