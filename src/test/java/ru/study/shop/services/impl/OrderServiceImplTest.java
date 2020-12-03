package ru.study.shop.services.impl;

import org.assertj.core.util.Lists;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.easymock.annotation.Mock;
import ru.study.shop.adapters.hibernate.OrderRepository;
import ru.study.shop.entities.Customer;
import ru.study.shop.entities.Order;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.OrderService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.util.Lists.emptyList;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.unitils.easymock.EasyMockUnitils.replay;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class OrderServiceImplTest {
    private static final List<Product> LIST_OF_PRODUCTS = Lists.newArrayList(
        new Product("Продукт 1", "Тип 1", "Материал 1", "-", "-", 1500L),
        new Product("Продукт 2", "Тип 2", "Материал 1", "-", "-", 2000L),
        new Product("Продукт 3", "Тип 1", "Материал 1", "-", "-", 3500L),
        new Product("Продукт 4", "Тип 2", "Материал 2", "-", "-", 4050L),
        new Product("Продукт 5", "Тип 3", "Материал 3", "-", "-", 6000L),
        new Product("Продукт 6", "Тип 3", "Материал 4", "-", "-", 7155L),
        new Product("Продукт 7", "Тип 3", "Материал 5", "-", "-", 8000L)
    );
    private static final Long EXPECTED_TOTAL_PRICE_OF_ORDER = 32205L;
    private static final Product PRODUCT_NOT_PRESENTED_IN_LIST = new Product("Товар не из списка", "", "", "", "", 666L);

    private static final Customer CUSTOMER_1 = new Customer("Имя 1", "Фамилия 1", "логин 1", "почта 1");
    private static final Customer CUSTOMER_2 = new Customer("Имя 2", "Фамилия 2", "логин 2", "почта 2");

    private static final LocalDateTime DATE_TIME_INITIAL = LocalDateTime.of(2020, Month.DECEMBER, 3, 12, 0);

    private static final boolean IS_DELIVERED = true;
    private static final boolean IS_NOT_DELIVERED = false;

    @Mock
    OrderRepository orderRepository;

    @TestSubject
    OrderService orderService;

    @Before
    public void setUp() {
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    public void getTotalPriceFromEmptyOrder() {
        Order order = new Order();
        Long totalPrice = orderService.getTotalPrice(order);

        assertEquals(0, totalPrice.longValue());
    }

    @Test
    public void getTotalPriceFromNonEmptyOrder() {
        Order order = new Order();
        order.setProductList(LIST_OF_PRODUCTS);

        Long totalPrice = orderService.getTotalPrice(order);

        assertEquals(EXPECTED_TOTAL_PRICE_OF_ORDER, totalPrice);
    }

    @Test
    public void findByProductWithEmptyOrderListAndEmptyProduct() {
        expect(orderRepository.findAll()).andReturn(emptyList());
        replay();

        List<Order> orderList = orderService.findByProduct(new Product());

        assertEquals(emptyList(), orderList);
    }

    @Test
    public void findByProductWithEmptyOrderList() {
        expect(orderRepository.findAll()).andReturn(emptyList());
        replay();

        List<Order> orderList = orderService.findByProduct(LIST_OF_PRODUCTS.get(0));

        assertEquals(emptyList(), orderList);
    }

    @Test
    public void findByProductNoProductInOrders() {
        List<Order> OrderList = Lists.list(
            new Order(CUSTOMER_1, LIST_OF_PRODUCTS, DATE_TIME_INITIAL, IS_DELIVERED),
            new Order(CUSTOMER_1, LIST_OF_PRODUCTS, DATE_TIME_INITIAL, IS_NOT_DELIVERED),
            new Order(CUSTOMER_2, LIST_OF_PRODUCTS, DATE_TIME_INITIAL, IS_DELIVERED)
        );

        expect(orderRepository.findAll()).andReturn(OrderList);
        replay();

        List<Order> obtainedOrderList = orderService.findByProduct(PRODUCT_NOT_PRESENTED_IN_LIST);

        assertEquals(emptyList(), obtainedOrderList);
    }

    @Test
    public void findByProductWithProductInAllOrders() {
        List<Order> OrderList = Lists.list(
            new Order(CUSTOMER_1, LIST_OF_PRODUCTS, DATE_TIME_INITIAL, IS_DELIVERED),
            new Order(CUSTOMER_1, LIST_OF_PRODUCTS, DATE_TIME_INITIAL, IS_NOT_DELIVERED),
            new Order(CUSTOMER_2, LIST_OF_PRODUCTS, DATE_TIME_INITIAL, IS_DELIVERED)
        );

        expect(orderRepository.findAll()).andReturn(OrderList);
        replay();

        List<Order> obtainedOrderList = orderService.findByProduct(LIST_OF_PRODUCTS.get(0));

        assertEquals(OrderList, obtainedOrderList);
    }

    @Test
    public void findByProductWithProductInSomeOrders() {
        List<Order> orderList = Lists.list(
            new Order(CUSTOMER_1, LIST_OF_PRODUCTS, DATE_TIME_INITIAL, IS_DELIVERED),
            new Order(CUSTOMER_2, LIST_OF_PRODUCTS, DATE_TIME_INITIAL, IS_DELIVERED)
        );
        orderList.add(new Order(CUSTOMER_1, Lists.newArrayList(PRODUCT_NOT_PRESENTED_IN_LIST), DATE_TIME_INITIAL, IS_NOT_DELIVERED));

        expect(orderRepository.findAll()).andReturn(orderList);
        replay();

        List<Order> obtainedOrderList = orderService.findByProduct(LIST_OF_PRODUCTS.get(0));

        orderList.remove(orderList.size() - 1);

        assertEquals(orderList, obtainedOrderList);
    }
}