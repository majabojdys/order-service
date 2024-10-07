package com.maja.orderService.orders;

import com.maja.orderService.emails.EmailService;
import com.maja.orderService.items.dtos.ItemDtoResponse;
import com.maja.orderService.items.exceptions.ItemNotFoundException;
import com.maja.orderService.items.repositories.ItemRepository;
import com.maja.orderService.orders.dtos.OrderDtoCreateRequest;
import com.maja.orderService.orders.dtos.OrderDtoResponse;
import com.maja.orderService.orders.exceptions.CancelCancelledOrderException;
import com.maja.orderService.orders.exceptions.CancelCompletedOrderException;
import com.maja.orderService.orders.exceptions.CompleteCancelledOrderException;
import com.maja.orderService.orders.exceptions.CompleteCompletedOrderException;
import com.maja.orderService.orders.exceptions.CompleteNotConfirmedOrderException;
import com.maja.orderService.orders.exceptions.ConfirmNotNewOrderException;
import com.maja.orderService.orders.exceptions.IncorrectConfirmationCodeException;
import com.maja.orderService.orders.exceptions.OrderNotFoundException;
import com.maja.orderService.orders.repositories.Order;
import com.maja.orderService.orders.repositories.OrderItem;
import com.maja.orderService.orders.repositories.OrderRepository;
import com.maja.orderService.users.exceptions.UserNotFoundException;
import com.maja.orderService.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, UserRepository userRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    void createOrderAndSendEmailWithConfirmationCode(OrderDtoCreateRequest orderDto, Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
        var order = new Order();
        var orderItems = orderDto.orderedItems().stream()
                .map(orderItemDto -> {
                    var item = itemRepository.findById(orderItemDto.id()).orElseThrow(() -> new ItemNotFoundException(orderItemDto.id().toString()));
                    return new OrderItem(order, item, orderItemDto.quantity());
                })
                .toList();
        order.setOrderItems(orderItems);
        order.setUser(user);
        orderRepository.save(order);
        emailService.sendConfirmationEmail(user.getEmail(), order.getId(), order.getConfirmationCode());
    }

    OrderDtoResponse getOrderDetails(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        var items = order.getOrderItems().stream()
                .map(orderItem -> {
                    var item = orderItem.getItem();
                    return new ItemDtoResponse(item.getName(), new BigDecimal(item.getPrice()), item.getColor(), orderItem.getQuantity());
                })
                .toList();
        var totalSum = items.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderDtoResponse(orderId, order.getStatus(), items, totalSum);
    }

    void confirmOrder(Long orderId, String confirmationCode) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        if (order.getStatus() != OrderStatus.NEW) {
            throw new ConfirmNotNewOrderException();
        }
        if(!order.getConfirmationCode().equals(confirmationCode)){
            throw new IncorrectConfirmationCodeException();
        }
        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    void cancelOrder(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        switch (order.getStatus()) {
            case COMPLETED -> throw new CancelCompletedOrderException();
            case CANCELLED -> throw new CancelCancelledOrderException();
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setFinishedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    void completeOrder(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        switch (order.getStatus()) {
            case COMPLETED -> throw new CompleteCompletedOrderException();
            case NEW -> throw new CompleteNotConfirmedOrderException();
            case CANCELLED -> throw new CompleteCancelledOrderException();
        }
        order.setStatus(OrderStatus.COMPLETED);
        order.setFinishedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
