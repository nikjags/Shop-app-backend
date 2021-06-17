package ru.study.shop.adapters.controllers;

import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.easymock.annotation.Mock;
import ru.study.shop.adapters.controllers.dto.OrderDto;
import ru.study.shop.adapters.controllers.rest.OrderController;
import ru.study.shop.adapters.hibernate.OrderRepository;
import ru.study.shop.entities.Customer;
import ru.study.shop.entities.Order;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.CustomerService;
import ru.study.shop.services.interfaces.OrderService;
import ru.study.shop.services.interfaces.ProductService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.time.LocalDateTime.of;
import static java.time.LocalDateTime.ofEpochSecond;
import static java.util.Objects.isNull;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.unitils.easymock.EasyMockUnitils.replay;
import static org.unitils.easymock.EasyMockUnitils.verify;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class OrderControllerTest {
    private static final int ZERO_AMOUNT = 0;
    private static final int ORDER_LIST_SIZE = 10;
    private static final LocalDateTime SOME_DATE = of(2021, 6, 10, 15, 35);
    private static final LocalDateTime MIN_LOCAL_DATE_TIME = OrderRepository.MIN_ORDER_DATE_TIME;
    private static final LocalDateTime MAX_LOCAL_DATE_TIME = OrderRepository.MAX_ORDER_DATE_TIME;
    private static final int MONTH_DIFF_BETWEEN_DATES = 1;
    private static final long NEGATIVE_ORDER_ID = -1L;
    private static final long ZERO_ORDER_ID = 0L;
    private static final long VALID_ORDER_ID = 1L;
    private static final long NEGATIVE_CUSTOMER_ID = -1L;
    private static final long ZERO_CUSTOMER_ID = 0L;
    private static final long VALID_CUSTOMER_ID = 1L;
    private static final long NEGATIVE_PRODUCT_ID = -1L;
    private static final long ZERO_PRODUCT_ID = 0L;
    private static final int MIN_PRODUCT_ID = 1;
    private static final long VALID_PRODUCT_ID = 1L;
    private static final int NEGATIVE_AMOUNT = -1;
    private static final int MIN_PRODUCT_AMOUNT = 1;
    private static final int VALID_PRODUCT_AMOUNT = 1;

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @TestSubject
    private OrderController orderController;

    @Before
    public void setUp() {
        orderController = new OrderController(
            orderService,
            customerService,
            productService);
    }

    @Test
    public void getAllOrdersWithoutDatesCallsFindAllOnce() {
        expect(orderService.findAll()).andReturn(getOrderList()).once();
        replay();

        orderController.getAllOrders(null, null);

        verify();
    }

    @Test
    public void getAllOrdersWithLowerNullDateCallsFindFromDateOnce() {
        expect(orderService.findAllToDate(SOME_DATE)).andReturn(getOrderList()).once();
        replay();

        orderController.getAllOrders(null, SOME_DATE);

        verify();
    }

    @Test
    public void getALlOrdersWithUpperNullDateCallsFindToDateOnce() {
        expect(orderService.findAllFromDate(SOME_DATE)).andReturn(getOrderList()).once();
        replay();

        orderController.getAllOrders(SOME_DATE, null);

        verify();
    }

    @Test
    public void getAllOrdersWithBothDatesCallsFindFromToDateOnce() {
        LocalDateTime latterThanSomeDate = SOME_DATE.plusMonths(MONTH_DIFF_BETWEEN_DATES);
        expect(orderService.findAllFromToDate(SOME_DATE, latterThanSomeDate))
            .andReturn(getOrderList()).once();
        replay();

        orderController.getAllOrders(SOME_DATE, latterThanSomeDate);

        verify();
    }

    @Test
    public void getOrderByIdWithInvalidIdThrowsException() {
        assertThrows(ResponseStatusException.class, () -> orderController.getOrderById(NEGATIVE_ORDER_ID));
        assertThrows(ResponseStatusException.class, () -> orderController.getOrderById(ZERO_ORDER_ID));
    }

    @Test
    public void getOrderByIdWithExistIdCallsFindByIdOnce() {
        expect(orderService.findById(VALID_ORDER_ID)).andReturn(Optional.of(new Order())).once();
        replay();

        orderController.getOrderById(VALID_ORDER_ID);

        verify();
    }

    @Test
    public void getOrderByIdWithNonExistIdCallsFindByIdOnceAndThrowsException() {
        expect(orderService.findById(VALID_ORDER_ID)).andReturn(Optional.empty()).once();
        replay();

        assertThrows(ResponseStatusException.class, () -> orderController.getOrderById(VALID_ORDER_ID));

        verify();
    }

    @Test
    public void createOrderWithEmptyDtoThrowsException() {
        OrderDto emptyDto = new OrderDto();
        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(emptyDto));
    }

    @Test
    public void createOrderWithCustomerIdConstraintViolationThrowsException() {
        OrderDto invalidDto = new OrderDto(null, getValidProductIdAmountMap(), SOME_DATE, false);

        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(invalidDto));
    }

    @Test
    public void createOrderWithProductIdAmountConstraintViolationThrowsException() {
        OrderDto invalidDto = getValidOrderDto();
        invalidDto.setProductIdAmountMap(null);
        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(invalidDto));

        invalidDto.setProductIdAmountMap(new HashMap<>());
        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(invalidDto));

        Map<Long, Integer> idAmountMapWithNegativeId = new HashMap<>();
        idAmountMapWithNegativeId.put(NEGATIVE_PRODUCT_ID, VALID_PRODUCT_AMOUNT);
        invalidDto.setProductIdAmountMap(idAmountMapWithNegativeId);
        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(invalidDto));

        Map<Long, Integer> idAmountMapWithZeroId = new HashMap<>();
        idAmountMapWithZeroId.put(ZERO_PRODUCT_ID, VALID_PRODUCT_AMOUNT);
        invalidDto.setProductIdAmountMap(idAmountMapWithZeroId);
        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(invalidDto));

        Map<Long, Integer> idAmountMapWithNegativeAmount = new HashMap<>();
        idAmountMapWithNegativeAmount.put(VALID_PRODUCT_ID, NEGATIVE_AMOUNT);
        invalidDto.setProductIdAmountMap(idAmountMapWithNegativeAmount);
        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(invalidDto));

        Map<Long, Integer> idAmountMapWithZeroAmount = new HashMap<>();
        idAmountMapWithNegativeAmount.put(VALID_PRODUCT_ID, ZERO_AMOUNT);
        invalidDto.setProductIdAmountMap(idAmountMapWithZeroAmount);
        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(invalidDto));
    }

    @Test
    public void createOrderWithOrderedTimeConstraintViolationThrowsException() {
        OrderDto invalidDto = new OrderDto(VALID_CUSTOMER_ID, getValidProductIdAmountMap(), null, false);

        assertThrows(ResponseStatusException.class, () -> orderController.createOrder(invalidDto));
    }

    @Test
    public void createOrderWithValidDtoCallsSaveOrderOnce() {
        OrderDto newOrderDto = new OrderDto(
            VALID_CUSTOMER_ID, getValidProductIdAmountMap(), SOME_DATE, true);

        Order order = mapDtoToOrder(newOrderDto);

        expect(customerService.findById(VALID_CUSTOMER_ID))
            .andReturn(Optional.of(new Customer()));
        expect(productService.findById(anyLong()))
            .andReturn(Optional.of(new Product()))
            .times(newOrderDto.getProductIdAmountMap().size());
        expect(orderService.saveOrder(anyObject(Order.class))).andReturn(order).once();
        replay();

        orderController.createOrder(newOrderDto);

        verify();
    }

    @Test
    public void editOrderInvalidOrderIdThrowsException() {
        OrderDto validDto = getValidOrderDto();

        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(NEGATIVE_ORDER_ID, validDto));
        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(ZERO_ORDER_ID, validDto));
    }

    @Test
    public void editOrderEmptyOrderDtoThrowsException() {
        OrderDto emptyDto = new OrderDto();

        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(VALID_ORDER_ID, emptyDto));
    }

    @Test
    public void editOrderInvalidCustomerIdInOrderDtoThrowsException() {
        OrderDto dtoWithNegativeCustomerId = getValidOrderDto();
        dtoWithNegativeCustomerId.setCustomerId(NEGATIVE_CUSTOMER_ID);

        OrderDto dtoWithZeroCustomerId = getValidOrderDto();
        dtoWithZeroCustomerId.setCustomerId(ZERO_CUSTOMER_ID);

        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(VALID_ORDER_ID, dtoWithNegativeCustomerId));
        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(VALID_ORDER_ID, dtoWithZeroCustomerId));
    }

    @Test
    public void editOrderInvalidProductIdAmountMapInOrderDtoThrowsException() {
        OrderDto dtoWithEmptyMap = getValidOrderDto();
        dtoWithEmptyMap.setProductIdAmountMap(new HashMap<>());
        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(VALID_ORDER_ID, dtoWithEmptyMap));

        OrderDto dtoWithNegativeProductId = getValidOrderDto();
        Map<Long, Integer> mapWithNegativeProductId = new HashMap<>();
        mapWithNegativeProductId.put(NEGATIVE_PRODUCT_ID, VALID_PRODUCT_AMOUNT);
        dtoWithNegativeProductId.setProductIdAmountMap(mapWithNegativeProductId);
        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(VALID_ORDER_ID, dtoWithNegativeProductId));

        OrderDto dtoWithZeroProductId = getValidOrderDto();
        Map<Long, Integer> mapWithZeroProductId = new HashMap<>();
        mapWithZeroProductId.put(ZERO_PRODUCT_ID, VALID_PRODUCT_AMOUNT);
        dtoWithZeroProductId.setProductIdAmountMap(mapWithZeroProductId);
        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(VALID_ORDER_ID, dtoWithZeroProductId));

        OrderDto dtoWithNegativeProductAmount = getValidOrderDto();
        Map<Long, Integer> mapWithNegativeProductAmount = new HashMap<>();
        mapWithNegativeProductAmount.put(VALID_PRODUCT_ID, NEGATIVE_AMOUNT);
        dtoWithNegativeProductAmount.setProductIdAmountMap(mapWithNegativeProductAmount);
        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(VALID_ORDER_ID, dtoWithNegativeProductAmount));

        OrderDto dtoWithZeroProductAmount = getValidOrderDto();
        Map<Long, Integer> mapWithZeroProductAmount = new HashMap<>();
        mapWithZeroProductAmount.put(VALID_PRODUCT_ID, ZERO_AMOUNT);
        dtoWithZeroProductAmount.setProductIdAmountMap(mapWithZeroProductAmount);
        assertThrows(ResponseStatusException.class, () -> orderController.editOrder(VALID_ORDER_ID, dtoWithZeroProductAmount));
    }

    @Test
    public void deleteOrderInvalidIdThrowsException() {
        assertThrows(ResponseStatusException.class, () -> orderController.deleteOrder(NEGATIVE_ORDER_ID));
        assertThrows(ResponseStatusException.class, () -> orderController.deleteOrder(ZERO_ORDER_ID));
    }

    @Test
    public void deleteOrderValidAndNonPresentIdThrowsException() {
        Order deletableOrder = new Order();

        expect(orderService.findById(VALID_ORDER_ID)).andReturn(Optional.empty()).atLeastOnce();
        replay();

        assertThrows(ResponseStatusException.class, () -> orderController.deleteOrder(VALID_ORDER_ID));
    }

    @Test
    public void deleteOrderValidAndPresentIdReturnsOk() {
        Order deletableOrder = new Order();
        deletableOrder.setId(VALID_ORDER_ID);

        expect(orderService.findById(VALID_ORDER_ID)).andReturn(Optional.of(deletableOrder)).atLeastOnce();
        orderService.deleteOrder(deletableOrder);
        expectLastCall();
        replay();

        ResponseEntity<Order> response = orderController.deleteOrder(deletableOrder.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    public Map<Long, Integer> getValidProductIdAmountMap() {
        Map<Long, Integer> idAmountMap = new HashMap<>();

        for (int i = 1; i < current().nextInt(MIN_PRODUCT_ID + 1, 10); i++) {
            idAmountMap.put(
                (long) i,
                current().nextInt(MIN_PRODUCT_AMOUNT, 10));
        }

        return idAmountMap;
    }

    private OrderDto getValidOrderDto() {
        return new OrderDto(VALID_ORDER_ID, getValidProductIdAmountMap(), SOME_DATE, true);
    }

    private Order mapDtoToOrder(OrderDto dto) {
        Customer customer = new Customer();
        customer.setId(VALID_CUSTOMER_ID);
        Order order = new Order(
            customer,
            mapToList(dto.getProductIdAmountMap()),
            dto.getOrderedTime(),
            dto.getDelivered());
        order.setId(VALID_ORDER_ID);
        return order;
    }

    private List<Product> mapToList(Map<Long, Integer> productIdAmountMap) {
        List<Product> productList = new ArrayList<>();

        for (Map.Entry<Long, Integer> pair : productIdAmountMap.entrySet()) {
            Long id = pair.getKey();

            for (int i = 0; i < pair.getValue(); i++) {
                Product product = new Product();
                product.setId(id);

                productList.add(product);
            }
        }

        return productList;
    }

    private List<Order> getOrderList() {
        return getOrderList(MIN_LOCAL_DATE_TIME, MAX_LOCAL_DATE_TIME, ORDER_LIST_SIZE);
    }

    private List<Order> getOrderList(LocalDateTime fromDate, LocalDateTime toDate, int size) {
        if (isNull(fromDate)) {
            fromDate = LocalDateTime.MIN;
        }
        if (isNull(toDate)) {
            fromDate = LocalDateTime.MAX;
        }

        List<Order> orderList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Order newOrder = new Order();
            newOrder.setOrderedTime(getRandomDateTime(fromDate, toDate));
            orderList.add(newOrder);
        }

        return orderList;
    }

    private LocalDateTime getRandomDateTime(LocalDateTime fromDate, LocalDateTime toDate) {
        long fromEpochSeconds = fromDate.toEpochSecond(ZoneOffset.UTC);
        long toEpochSeconds = toDate.toEpochSecond(ZoneOffset.UTC);
        long rndEpochSeconds = current().nextLong(toEpochSeconds - fromEpochSeconds) + fromEpochSeconds;

        return ofEpochSecond(rndEpochSeconds, 0, ZoneOffset.UTC);
    }
}