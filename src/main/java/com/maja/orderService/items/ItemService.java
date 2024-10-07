package com.maja.orderService.items;

import com.maja.orderService.items.dtos.ItemDtoCreateRequest;
import com.maja.orderService.items.repositories.Item;
import com.maja.orderService.items.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Service
class ItemService {

    private static final int TWO_DECIMAL_PLACES = 2;
    private final ItemRepository itemRepository;

    ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    void createItem(ItemDtoCreateRequest itemDto){
        var price = itemDto.price().setScale(TWO_DECIMAL_PLACES, RoundingMode.DOWN).toString();
        var item = new Item(itemDto.name(), price, itemDto.color());
        itemRepository.save(item);
    }
}
