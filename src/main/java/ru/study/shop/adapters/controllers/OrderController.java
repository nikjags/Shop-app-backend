package ru.study.shop.adapters.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.study.shop.entities.Order;
import ru.study.shop.services.interfaces.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Order>> getAllOrdersByDate(
        @RequestParam(name = "from", required = false) String strFromTime,
        @RequestParam(name = "to", required = false) String strToTime) {

        LocalDateTime dateTimeFrom = LocalDateTime.MIN;
        LocalDateTime dateTimeTo = LocalDateTime.MAX;

        if (Objects.isNull(strFromTime) && Objects.isNull(strToTime))
            return ResponseEntity.ok().body(orderService.findAll());
        if(Objects.nonNull(strFromTime)) {
            dateTimeFrom = LocalDateTime.parse(strFromTime);
        }
        if (Objects.nonNull(strToTime)) {
            dateTimeTo = LocalDateTime.parse(strToTime);
        }

        LocalDateTime finalDateTimeFrom = dateTimeTo;
        return ResponseEntity.ok().body(
            orderService.findAllFromDate(dateTimeFrom)
                .stream()
                .filter(order -> order.getOrderedTime().isBefore(finalDateTimeFrom))
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long orderId) {
        Optional<Order> order = orderService.findById(orderId);
        return order
            .map(value -> ResponseEntity.ok().body(value))
            .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
}