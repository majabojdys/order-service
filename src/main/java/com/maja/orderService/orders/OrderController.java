package com.maja.orderService.orders;

import com.maja.orderService.orders.dtos.OrderDtoCreateRequest;
import com.maja.orderService.orders.dtos.OrderDtoCreateResponse;
import com.maja.orderService.orders.dtos.OrderDtoResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    OrderDtoCreateResponse createOrder(@Valid @RequestBody OrderDtoCreateRequest orderDto,
                                       @RequestParam Long userId) {
        return orderService.createOrderAndSendEmailWithConfirmationCode(orderDto, userId);
    }

    @GetMapping("/{id}")
    OrderDtoResponse getOrder(@PathVariable Long id) {
        return orderService.getOrderDetails(id);
    }

    @PostMapping("/{id}/confirm")
    void confirmOrder(@PathVariable Long id,
                      @RequestParam String confirmationCode) {
        orderService.confirmOrder(id, confirmationCode);
    }

    @PostMapping("/{id}/cancel")
    void cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }

    @PostMapping("/{id}/complete")
    void completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
    }
}
