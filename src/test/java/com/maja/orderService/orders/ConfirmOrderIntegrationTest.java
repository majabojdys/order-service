package com.maja.orderService.orders;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ConfirmOrderIntegrationTest extends IntegrationTest {

    @Test
    void shouldConfirmOrder() {
        // given
        var order = OrderFactory.getOrderWithNewStatus();
        orderRepository.save(order);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + order.getId() + "/confirm?confirmationCode=" + order.getConfirmationCode(), null, Void.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var orders = orderRepository.findAll();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(OrderStatus.CONFIRMED, orders.getFirst().getStatus());
    }

    @Test
    void shouldReturn404NotFoundWhenConfirmOrderWhichDoesNotExist() {
        // given
        var nonExistingOrderId = 123L;

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + nonExistingOrderId + "/confirm?confirmationCode=abc", null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("ORDER_NOT_FOUND", response.getBody().errorCode());
        Assertions.assertEquals("Order with id 123 not found", response.getBody().errorDescription());
    }

    @Test
    void shouldReturn400BadRequestWhenConfirmOrderWithIncorrectConfirmationCode() {
        // given
        var order = OrderFactory.getOrderWithNewStatus();
        orderRepository.save(order);
        var incorrectConfirmationCode = "wrong";

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + order.getId() + "/confirm?confirmationCode=" + incorrectConfirmationCode, null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("INCORRECT_CONFIRMATION_CODE", response.getBody().errorCode());
        Assertions.assertEquals("Confirmation code is incorrect", response.getBody().errorDescription());
    }
}
