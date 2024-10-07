package com.maja.orderService.orders;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import com.maja.orderService.items.ItemsFactory;
import com.maja.orderService.orders.dtos.OrderDtoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

class GetOrderIntegrationTest extends IntegrationTest {

    @Test
    void shouldGetOrder(){
        // given
        var item1 = ItemsFactory.getItem("cup");
        var item2 = ItemsFactory.getItem("puzzle");
        itemRepository.saveAll(List.of(item1, item2));
        var order = OrderFactory.getOrderWithOrderItems(item1, item2);
        orderRepository.save(order);

        // when
        var response = restTemplate.getForEntity(getLocalhost() + "/api/v1/orders/" + order.getId(), OrderDtoResponse.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var responseBody = response.getBody();
        Assertions.assertEquals(order.getId(), responseBody.id());
        Assertions.assertEquals(OrderStatus.NEW, responseBody.orderStatus());
        Assertions.assertEquals(item1.getPrice(), responseBody.items().get(0).price().toString());
        Assertions.assertEquals(1, responseBody.items().get(0).quantity());
        Assertions.assertEquals(item1.getColor(), responseBody.items().get(0).color());
        Assertions.assertEquals(item1.getName(), responseBody.items().get(0).name());
        Assertions.assertEquals(item2.getPrice(), responseBody.items().get(1).price().toString());
        Assertions.assertEquals(2, responseBody.items().get(1).quantity());
        Assertions.assertEquals(item2.getColor(), responseBody.items().get(1).color());
        Assertions.assertEquals(item2.getName(), responseBody.items().get(1).name());
        Assertions.assertEquals("36.75", responseBody.totalSum().toString());
    }

    @Test
    void shouldReturn404NotFoundWhenGetOrderWhichDoesNotExist(){
        // given
        var nonExistingOrderId = 123L;

        // when
        var response = restTemplate.getForEntity(getLocalhost() + "/api/v1/orders/" + nonExistingOrderId, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("ORDER_NOT_FOUND", response.getBody().errorCode());
        Assertions.assertEquals("Order with id 123 not found", response.getBody().errorDescription());
    }
}
