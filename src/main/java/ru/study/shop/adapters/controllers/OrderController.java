package ru.study.shop.adapters.controllers;

import org.apache.commons.validator.GenericValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.study.shop.adapters.controllers.dto.OrderDto;
import ru.study.shop.entities.Customer;
import ru.study.shop.entities.Order;
import ru.study.shop.services.interfaces.CustomerService;
import ru.study.shop.services.interfaces.OrderService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Locale DATE_TIME_LOCALE = DateTimeFormatter.ISO_DATE_TIME.getLocale();
    private static final String NO_CUSTOMER_MESSAGE = "no such customer with provided ID";
    private static final String NO_SUCH_ORDER_MESSAGE = "no such order with provided ID";
    private static final String INVALID_ID_MESSAGE = "invalid ID; must be more than 0";

    private final OrderService orderService;

    private final CustomerService customerService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Order>> getAllOrdersByDate(
        @RequestParam(name = "from", required = false) String strFromTime,
        @RequestParam(name = "to", required = false) String strToTime) {

        LocalDateTime dateTimeFrom = LocalDateTime.MIN;
        LocalDateTime dateTimeTo = LocalDateTime.MAX;

        if (Objects.isNull(strFromTime) && Objects.isNull(strToTime)) {
            return ResponseEntity.ok().body(orderService.findAll());
        }
        if (nonNull(strFromTime) && GenericValidator.isDate(strFromTime, DATE_TIME_LOCALE)) {
            dateTimeFrom = LocalDateTime.parse(strFromTime);
        }
        if (nonNull(strToTime) && GenericValidator.isDate(strToTime, DATE_TIME_LOCALE)) {
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

    @PostMapping(value = "/new", produces = "application/json")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto newOrderDto) {
        validateOrderDto(newOrderDto);

        Customer orderCustomer = getOrderCustomer(newOrderDto.getCustomerId());

        Order newOrder = mapDtoToOrder(newOrderDto, orderCustomer);

        return ResponseEntity.ok(orderService.saveOrder(newOrder));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> editOrder(@PathVariable("id") Long orderId, @RequestBody OrderDto orderDto) {
        validateOrderId(orderId);

        Order editableOrder = checkForPresenceAndGetOrder(orderId);

        boolean isEdited = updateOrder(editableOrder, orderDto);

        if (isEdited) {
            orderService.saveOrder(editableOrder);
        }

        return ResponseEntity.ok(editableOrder);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("id") Long orderId) {
        validateOrderId(orderId);

        Order deletableOrder = checkForPresenceAndGetOrder(orderId);
        orderService.deleteOrder(deletableOrder);

        return ResponseEntity.ok().build();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private boolean updateOrder(Order editableOrder, OrderDto orderDto) {
        boolean isChanged = false;

        if (nonNull(orderDto.getProductList())
            && validateDtoProperty(orderDto, "productList")) {

            editableOrder.setProductList(orderDto.getProductList());
            isChanged = true;
        }
        if (nonNull(orderDto.getOrderedTime())
            && validateDtoProperty(orderDto, "orderedTime")) {

            editableOrder.setProductList(orderDto.getProductList());
            isChanged = true;
        }
        if (validateDtoProperty(orderDto, "delivered")) {

            editableOrder.setDelivered(orderDto.isDelivered());
            isChanged = true;
        }

        return isChanged;
    }

    private boolean validateDtoProperty(OrderDto orderDto, String propertyName) {
        return validator.validateProperty(orderDto, propertyName).isEmpty();
    }

    private void validateOrderId(Long orderId) {
        if (orderId < 1) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, INVALID_ID_MESSAGE
            );
        }
    }

    private Order mapDtoToOrder(OrderDto newOrderDto, Customer orderCustomer) {
        return new Order(
            orderCustomer,
            newOrderDto.getProductList(),
            newOrderDto.getOrderedTime(),
            newOrderDto.isDelivered()
        );
    }

    private void validateOrderDto(OrderDto newOrder) {
        Set<ConstraintViolation<OrderDto>> violations = validator.validate(newOrder);

        if (!violations.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (ConstraintViolation<OrderDto> violation : violations) {
                str.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, str.toString());
        }
    }

    private Order checkForPresenceAndGetOrder(Long orderId) {
        Optional<Order> optionalOrder = orderService.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, NO_SUCH_ORDER_MESSAGE
            );
        }

        return optionalOrder.get();
    }

    private Customer getOrderCustomer(Long customerId) {
        Optional<Customer> optionalCustomer = customerService.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, NO_CUSTOMER_MESSAGE
            );
        }

        return optionalCustomer.get();
    }
}
