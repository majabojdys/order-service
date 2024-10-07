package com.maja.orderService.orders;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class CancelOrderIntegrationTest extends IntegrationTest {

    @Test
    void shouldCancelOrder() {
        // given
        var order = OrderFactory.getOrderWithNewStatus();
        orderRepository.save(order);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + order.getId() + "/cancel", null, Void.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var orders = orderRepository.findAll();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(OrderStatus.CANCELLED, orders.getFirst().getStatus());
    }

    @Test
    void shouldReturn404NotFoundWhenCancelOrderWhichDoesNotExist() {
        // given
        var nonExistingOrderId = 123L;

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + nonExistingOrderId + "/cancel", null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("ORDER_NOT_FOUND", response.getBody().errorCode());
        Assertions.assertEquals("Order with id 123 not found", response.getBody().errorDescription());
    }

    @Test
    void shouldReturn409ConflictWhenCancelOrderWhichIsCompleted() {
        // given
        var completedOrder = OrderFactory.getOrderWithCompletedStatus();
        orderRepository.save(completedOrder);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + completedOrder.getId() + "/cancel", null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("CANCEL_COMPLETED_CONFLICT", response.getBody().errorCode());
        Assertions.assertEquals("Completed order can not be cancelled", response.getBody().errorDescription());
    }

    @Test
    void shouldReturn409ConflictWhenCancelOrderWhichIsCancelled() {
        // given
        var cancelledOrder = OrderFactory.getOrderWithCancelledStatus();
        orderRepository.save(cancelledOrder);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders/" + cancelledOrder.getId() + "/cancel", null, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("CANCEL_CANCELLED_CONFLICT", response.getBody().errorCode());
        Assertions.assertEquals("Order has already been cancelled", response.getBody().errorDescription());
    }
}
