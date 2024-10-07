package com.maja.orderService.items;

import com.maja.orderService.items.dtos.ItemDtoCreateRequest;
import com.maja.orderService.items.dtos.ItemListDtoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    ItemListDtoResponse getItems(Pageable pageable){
        return itemService.getItems(pageable);
    }
}
