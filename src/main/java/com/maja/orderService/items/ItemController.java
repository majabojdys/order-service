package com.maja.orderService.items;

import com.maja.orderService.items.dtos.ItemDtoCreateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
class ItemController {

    private final ItemService itemService;

    ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    void createItem(@Valid @RequestBody ItemDtoCreateRequest itemDto){
        itemService.createItem(itemDto);
    }
}
