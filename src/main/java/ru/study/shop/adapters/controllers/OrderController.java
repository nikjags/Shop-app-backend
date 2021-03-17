package ru.study.shop.adapters.controllers;

import org.apache.commons.validator.GenericValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.study.shop.entities.Order;
import ru.study.shop.services.interfaces.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private static final Locale DATE_TIME_LOCALE = DateTimeFormatter.ISO_DATE_TIME.getLocale();

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
        if (Objects.nonNull(strFromTime) && GenericValidator.isDate(strFromTime, DATE_TIME_LOCALE)) {
            dateTimeFrom = LocalDateTime.parse(strFromTime);
        }
        if (Objects.nonNull(strToTime) && GenericValidator.isDate(strToTime, DATE_TIME_LOCALE)) {
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
        return orderService.findById(orderId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
}
