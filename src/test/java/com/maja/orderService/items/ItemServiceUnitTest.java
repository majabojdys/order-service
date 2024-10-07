package com.maja.orderService.items;

import com.maja.orderService.items.repositories.Item;
import com.maja.orderService.items.repositories.ItemRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

class ItemServiceUnitTest {

    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private final ItemService itemService = new ItemService(itemRepository);

    @ParameterizedTest
    @CsvSource({
            "12.25, 12.25",
            "12.259, 12.25",
            "12.251, 12.25",
            "12.2, 12.20"
    })
    void shouldCreateOrderWithOrderItemsAndRoundDownPriceIfNecessary(Double inputPrice, String expectedPrice) {
        // given
        var item = ItemsFactory.getItemDto(inputPrice);

        // when
        itemService.createItem(item);

        // then
        var savedItem = new Item(item.name(), expectedPrice, item.color());
        Mockito.verify(itemRepository).save(savedItem);
    }
}
