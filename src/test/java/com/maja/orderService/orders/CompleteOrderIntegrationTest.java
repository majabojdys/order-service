package com.maja.orderService.orders;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CompleteOrderIntegrationTest extends IntegrationTest {

    @Test
    void shouldCompleteOrder() {
        // given
        var confirmedOrder = OrderFactory.getOrderWithConfirmedStatus();
        orderRepository.save(confirmedOrder);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + confirmedOrder.getId() + "/complete", null, Void.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var orders = orderRepository.findAll();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(OrderStatus.COMPLETED, orders.getFirst().getStatus());
        Assertions.assertTrue(orders.getFirst().getFinishedAt().isPresent());
    }

    @Test
    void shouldReturn404NotFoundWhenCompleteOrderWhichDoesNotExist() {
        // given
        var nonExistingOrderId = 123L;

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + nonExistingOrderId + "/complete", null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("ORDER_NOT_FOUND", response.getBody().errorCode());
        Assertions.assertEquals("Order with id 123 not found", response.getBody().errorDescription());
    }

    @Test
    void shouldReturn409ConflictWhenCompleteOrderWhichIsCompleted() {
        // given
        var completedOrder = OrderFactory.getOrderWithCompletedStatus();
        orderRepository.save(completedOrder);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + completedOrder.getId() + "/complete", null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("COMPLETE_COMPLETED_CONFLICT", response.getBody().errorCode());
        Assertions.assertEquals("Order has already been completed", response.getBody().errorDescription());
    }

    @Test
    void shouldReturn409ConflictWhenCompleteOrderWhichIsNotConfirmed() {
        // given
        var order = OrderFactory.getOrderWithNewStatus();
        orderRepository.save(order);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + order.getId() + "/complete", null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("COMPLETE_NOT_CONFIRMED_CONFLICT", response.getBody().errorCode());
        Assertions.assertEquals("Only confirmed order can be completed", response.getBody().errorDescription());
    }

    @Test
    void shouldReturn409ConflictWhenCompleteOrderWhichIsCancelled() {
        // given
        var completedOrder = OrderFactory.getOrderWithCancelledStatus();
        orderRepository.save(completedOrder);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + completedOrder.getId() + "/complete", null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("COMPLETE_CANCELLED_CONFLICT", response.getBody().errorCode());
        Assertions.assertEquals("Can not complete cancelled order", response.getBody().errorDescription());
    }
}
