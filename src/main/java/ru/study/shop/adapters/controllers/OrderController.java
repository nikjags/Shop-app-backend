package ru.study.shop.adapters.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.study.shop.adapters.controllers.dto.OrderDto;
import ru.study.shop.adapters.controllers.utils.dto_validation.groups.OnCreate;
import ru.study.shop.adapters.controllers.utils.dto_validation.groups.OnUpdate;
import ru.study.shop.entities.Customer;
import ru.study.shop.entities.Order;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.CustomerService;
import ru.study.shop.services.interfaces.OrderService;
import ru.study.shop.services.interfaces.ProductService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Class<OnCreate> CREATE_OPTION = OnCreate.class;
    private static final Class<OnUpdate> UPDATE_OPTION = OnUpdate.class;

    private static final String INVALID_ORDER_ID_MESSAGE = "invalid order ID; must be more than 0";
    private static final String NO_SUCH_PRODUCT_MESSAGE = "no such product with provided ID: ";
    private static final String NO_CUSTOMER_MESSAGE = "no such customer with provided ID: ";
    private static final String NO_SUCH_ORDER_MESSAGE = "no such order with provided ID: ";
    private static final String NO_PROPERTIES_TO_UPDATE_MESSAGE = "no properties to update in request body";

    private final OrderService orderService;

    private final CustomerService customerService;

    private final ProductService productService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public OrderController(OrderService orderService, CustomerService customerService, ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Order>> getAllOrders(
        @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDT,
        @RequestParam(name = "toDate", required = false) LocalDateTime toDT) {

        List<Order> result;

        if (isNull(fromDT) && isNull(toDT)) {
            result = orderService.findAll();
        } else if (nonNull(fromDT) && isNull(toDT)) {
            result = orderService.findAllFromDate(fromDT);
        } else if (isNull(fromDT)) {
            result = orderService.findAllToDate(toDT);
        } else {
            result = orderService.findAllFromToDate(fromDT, toDT);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long orderId) {
        validateOrderId(orderId);

        return orderService.findById(orderId)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_PRODUCT_MESSAGE + orderId));
    }

    @PostMapping(value = "/new", produces = "application/json")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto newOrderDto) {
        validateOrderDto(newOrderDto, CREATE_OPTION);

        Order newOrder = mapDtoToOrder(newOrderDto);

        return ResponseEntity.ok(orderService.saveOrder(newOrder));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> editOrder(@PathVariable("id") Long orderId, @RequestBody OrderDto orderDto) {
        validateOrderId(orderId);
        validateOrderDto(orderDto, UPDATE_OPTION);

        Order editableOrder = checkForPresenceAndGetOrder(orderId);
        updateOrder(editableOrder, orderDto);

        return ResponseEntity.ok(orderService.saveOrder(editableOrder));
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

    private void validateOrderId(Long orderId) {
        if (orderId < 1) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                INVALID_ORDER_ID_MESSAGE);
        }
    }

    private void validateOrderDto(OrderDto dto, Class<?> option) {
        if (isNull(dto)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                NO_PROPERTIES_TO_UPDATE_MESSAGE);
        }

        if (isNull(option) || !option.equals(UPDATE_OPTION)) {
            option = CREATE_OPTION;
        }

        Set<ConstraintViolation<OrderDto>> violations = validator.validate(dto, option);

        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("violations: [");
            for (ConstraintViolation<OrderDto> violation : violations) {
                stringBuilder
                    .append(violation.getPropertyPath())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("; ");
            }
            stringBuilder.append("]");

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, stringBuilder.toString());
        }
    }

    private void updateOrder(Order editableOrder, OrderDto orderDto) {
        if (nonNull(orderDto.getCustomerId()) &&
            !orderDto.getCustomerId().equals(editableOrder.getCustomer().getId())) {

            editableOrder.setCustomer(
                checkForPresenceAndGetCustomer(orderDto.getCustomerId()));
        }

        if (nonNull(orderDto.getProductIdAmountMap())) {

            editableOrder.setProductList(
                dtoProductMapToList(orderDto.getProductIdAmountMap()));
        }
        if (nonNull(orderDto.getOrderedTime())) {

            editableOrder.setOrderedTime(orderDto.getOrderedTime());
        }
        if (nonNull(orderDto.getDelivered()) &&
            editableOrder.isDelivered() != orderDto.getDelivered()) {

            editableOrder.setDelivered(orderDto.getDelivered());
        }
    }

    private Order mapDtoToOrder(OrderDto newOrderDto) {
        Optional<Customer> optionalCustomer = customerService.findById(newOrderDto.getCustomerId());
        if (!optionalCustomer.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, NO_CUSTOMER_MESSAGE);
        }

        return new Order(
            optionalCustomer.get(),
            dtoProductMapToList(newOrderDto.getProductIdAmountMap()),
            newOrderDto.getOrderedTime(),
            newOrderDto.getDelivered());
    }

    private List<Product> dtoProductMapToList(Map<Long, Integer> productIdAmount) {
        List<Product> productList = new ArrayList<>();

        for (Entry<Long, Integer> entry : productIdAmount.entrySet()) {
            Long productId = entry.getKey();

            Product product = checkForPresenceAndGetProduct(productId);

            for (int i = 0; i < entry.getValue(); i++) {
                productList.add(product);
            }
        }

        return productList;
    }

    private Order checkForPresenceAndGetOrder(Long orderId) {
        return orderService
            .findById(orderId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    NO_SUCH_ORDER_MESSAGE + orderId));
    }

    private Customer checkForPresenceAndGetCustomer(Long customerId) {
        return customerService
            .findById(customerId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    NO_CUSTOMER_MESSAGE + customerId));
    }

    private Product checkForPresenceAndGetProduct(Long productId) {
        return productService
            .findById(productId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    NO_SUCH_PRODUCT_MESSAGE + productId));
    }
}
