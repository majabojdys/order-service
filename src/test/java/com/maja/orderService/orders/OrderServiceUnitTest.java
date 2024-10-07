package com.maja.orderService.orders;

import com.maja.orderService.emails.EmailService;
import com.maja.orderService.items.ItemsFactory;
import com.maja.orderService.items.repositories.ItemRepository;
import com.maja.orderService.orders.repositories.Order;
import com.maja.orderService.orders.repositories.OrderRepository;
import com.maja.orderService.users.UserFactory;
import com.maja.orderService.users.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

class OrderServiceUnitTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final EmailService emailService = Mockito.mock(EmailService.class);
    private final OrderService orderService = new OrderService(orderRepository, itemRepository, userRepository, emailService);

    @Test
    void shouldCreateOrderWithOrderItems() {
        // given
        var orderDto = OrderFactory.getOrderDtoWithOrderItemsDto();
        var item1 = ItemsFactory.getItem("cup");
        var item2 = ItemsFactory.getItem("puzzle");
        var user = UserFactory.getUserWithAddress();
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        Mockito.when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        var captor = ArgumentCaptor.forClass(Order.class);

        // when
        orderService.createOrderAndSendEmailWithConfirmationCode(orderDto, 1L);

        // then
        Mockito.verify(orderRepository).save(captor.capture());
        var savedOrder = captor.getValue();
        Assertions.assertEquals(OrderStatus.NEW, savedOrder.getStatus());
        Assertions.assertEquals(1, savedOrder.getOrderItems().get(0).getQuantity());
        Assertions.assertEquals(2, savedOrder.getOrderItems().get(1).getQuantity());
        Assertions.assertEquals(item1, savedOrder.getOrderItems().get(0).getItem());
        Assertions.assertEquals(item2, savedOrder.getOrderItems().get(1).getItem());
        Mockito.verify(emailService).sendConfirmationEmail(user.getEmail(), savedOrder.getId(), savedOrder.getConfirmationCode());
    }
}
