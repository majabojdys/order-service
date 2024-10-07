package com.maja.orderService.orders;

import com.maja.orderService.items.repositories.Item;
import com.maja.orderService.orders.dtos.OrderDtoCreateRequest;
import com.maja.orderService.orders.dtos.OrderItemDtoCreateRequest;
import com.maja.orderService.orders.repositories.Order;
import com.maja.orderService.orders.repositories.OrderItem;

import java.util.List;

public class OrderFactory {

    public static Order getOrderWithNewStatus() {
        return new Order();
    }

    public static Order getOrderWithCompletedStatus() {
        var order = new Order();
        order.setStatus(OrderStatus.COMPLETED);
        return order;
    }

    public static Order getOrderWithCancelledStatus() {
        var order = new Order();
        order.setStatus(OrderStatus.CANCELLED);
        return order;
    }

    public static Order getOrderWithConfirmedStatus() {
        var order = new Order();
        order.setStatus(OrderStatus.CONFIRMED);
        return order;
    }

    public static Order getOrderWithOrderItems(Item item1, Item item2) {
        var order = new Order();
        var orderItems = List.of(new OrderItem(order, item1, 1), new OrderItem(order, item2, 2));
        order.setOrderItems(orderItems);
        return order;
    }

    public static OrderDtoCreateRequest getOrderDtoWithOrderItemsDto() {
        var orderItemsDto = List.of(new OrderItemDtoCreateRequest(1L, 1), new OrderItemDtoCreateRequest(2L, 2));
        return new OrderDtoCreateRequest(orderItemsDto);
    }

    public static OrderDtoCreateRequest getOrderDtoWithOrderItemsDto(Long itemId1, Long itemId2) {
        var orderItemsDto = List.of(new OrderItemDtoCreateRequest(itemId1, 1), new OrderItemDtoCreateRequest(itemId2, 2));
        return new OrderDtoCreateRequest(orderItemsDto);
    }
}
