package com.maja.orderService.orders;

import com.maja.orderService.IntegrationTest;
import com.maja.orderService.commons.Error;
import com.maja.orderService.items.ItemsFactory;
import com.maja.orderService.orders.dtos.OrderDtoCreateRequest;
import com.maja.orderService.orders.dtos.OrderItemDtoCreateRequest;
import com.maja.orderService.users.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

class CreateOrderIntegrationTest extends IntegrationTest {

    @Test
    void shouldCreateOrder() {
        // given
        var item1 = ItemsFactory.getItem("cup");
        var item2 = ItemsFactory.getItem("puzzle");
        itemRepository.saveAll(List.of(item1, item2));
        var orderDto = OrderFactory.getOrderDtoWithOrderItemsDto(item1.getId(), item2.getId());
        var user = UserFactory.getUserWithAddress();
        userRepository.save(user);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders?userId=" + user.getId(), orderDto, Void.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var orders = orderRepository.findAll();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(OrderStatus.NEW, orders.getFirst().getStatus());
        Assertions.assertEquals(item1, orders.getFirst().getOrderItems().get(0).getItem());
        Assertions.assertEquals(item2, orders.getFirst().getOrderItems().get(1).getItem());
        Assertions.assertEquals(user, orders.getFirst().getUser());

        var message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirmation code for order number " + orders.getFirst().getId());
        message.setText("Your confirmation code for order " + orders.getFirst().getId() + " is: " + orders.getFirst().getConfirmationCode());
        message.setFrom("USERNAME");
        Mockito.verify(mailSender).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    void shouldReturn404NotFoundWhenCreateOrderWithoutItems() {
        // given
        var orderItemsDto = List.of(new OrderItemDtoCreateRequest(123L, 1));
        var orderDto = new OrderDtoCreateRequest(orderItemsDto);
        var user = UserFactory.getUserWithAddress();
        userRepository.save(user);

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders?userId=" + user.getId(), orderDto, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("ITEM_NOT_FOUND", response.getBody().errorCode());
        Assertions.assertEquals("Item with id 123 not found", response.getBody().errorDescription());
    }

    @Test
    void shouldReturn404NotFoundWhenCreateOrderWithoutExistingUser() {
        // given
        var orderItemsDto = List.of(new OrderItemDtoCreateRequest(123L, 1));
        var orderDto = new OrderDtoCreateRequest(orderItemsDto);
        var nonExistingUserId = 123;

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders?userId=" + nonExistingUserId, orderDto, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("USER_NOT_FOUND", response.getBody().errorCode());
        Assertions.assertEquals("User with id 123 not found", response.getBody().errorDescription());
    }

    @Test
    void shouldValidateRequest() {
        // given
        var userId = 123;
        var orderDto = new OrderDtoCreateRequest(List.of(new OrderItemDtoCreateRequest(null, 0)));

        // when
        var response = restTemplate.postForEntity(getLocalhost() + "/api/v1/orders?userId=" + userId, orderDto, Error.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        Assertions.assertEquals("REQUEST_VALIDATION_ERRORS", body.errorCode());
        Assertions.assertEquals("[orderedItems[0].id must not be null, orderedItems[0].quantity must be greater than or equal to 1]", body.errorDescription());
    }
}
