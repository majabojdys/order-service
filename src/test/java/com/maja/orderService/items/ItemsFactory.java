package com.maja.orderService.items;

import com.maja.orderService.items.dtos.ItemDtoCreateRequest;
import com.maja.orderService.items.repositories.Item;

import java.math.BigDecimal;

public class ItemsFactory {

    public static Item getItem(String name) {
        return new Item(name, "12.25", "white");
    }

    public static ItemDtoCreateRequest getItemDto() {
        return new ItemDtoCreateRequest("cup", new BigDecimal("12.25"), "white");
    }

    public static ItemDtoCreateRequest getItemDto(Double price) {
        return new ItemDtoCreateRequest("cup", BigDecimal.valueOf(price), "white");
    }
}
