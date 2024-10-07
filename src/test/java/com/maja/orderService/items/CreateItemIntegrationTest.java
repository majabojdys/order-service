package com.maja.orderService.items;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import com.maja.orderService.items.dtos.ItemDtoCreateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

class CreateItemIntegrationTest extends IntegrationTest {

    @Test
    void shouldCreateItem(){
        // given
        var itemDto = ItemsFactory.getItemDto();

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/items", itemDto, Void.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var items = itemRepository.findAll();
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(itemDto.name(), items.getFirst().getName());
        Assertions.assertEquals("12.25", items.getFirst().getPrice());
        Assertions.assertEquals(itemDto.color(), items.getFirst().getColor());
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
