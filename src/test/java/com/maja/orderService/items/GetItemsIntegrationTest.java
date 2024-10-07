package com.maja.orderService.items;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import com.maja.orderService.items.dtos.ItemDtoCreateRequest;
import com.maja.orderService.items.dtos.ItemListDtoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

class GetItemsIntegrationTest extends IntegrationTest {

    @Test
    void shouldGetItems(){
        // given
        var item1 = ItemsFactory.getItem("cup");
        var item2 = ItemsFactory.getItem("puzzle");
        itemRepository.saveAll(List.of(item1, item2));

        // when
        var response = restTemplate.getForEntity(getLocalhost() + "/api/v1/items", ItemListDtoResponse.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var body = response.getBody();
        Assertions.assertEquals(2, body.items().size());
        Assertions.assertEquals(item1.getName(), body.items().getFirst().name());
        Assertions.assertEquals(item1.getColor(), body.items().getFirst().color());
        Assertions.assertEquals(item1.getPrice(), body.items().getFirst().price());
        Assertions.assertEquals(item2.getName(), body.items().getLast().name());
        Assertions.assertEquals(item2.getColor(), body.items().getLast().color());
        Assertions.assertEquals(item2.getPrice(), body.items().getLast().price());
    }

    @Test
    void shouldValidateRequest(){
        // given
        var itemDto = new ItemDtoCreateRequest(null, BigDecimal.ZERO, null);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/items", itemDto, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        Assertions.assertEquals("REQUEST_VALIDATION_ERRORS", body.errorCode());
        Assertions.assertEquals("[color must not be blank, name must not be blank, price must be greater than or equal to 0.01]", body.errorDescription());
    }
}
