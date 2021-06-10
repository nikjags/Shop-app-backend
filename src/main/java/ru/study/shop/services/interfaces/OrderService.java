package ru.study.shop.services.interfaces;

import ru.study.shop.entities.Customer;
import ru.study.shop.entities.Order;
import ru.study.shop.entities.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();

    Optional<Order> findById(Long orderId);

    List<Order> findAllFromDate(LocalDateTime fromDate);

    List<Order> findAllToDate(LocalDateTime toDate);

    List<Order> findAllFromToDate(LocalDateTime from, LocalDateTime to);

    List<Order> findByCustomer(Customer customer);

    List<Order> findByProduct(Product product);

    Order saveOrder(Order order);

    List<Order> saveAll(List<Order> orders);

    void deleteOrder(Order order);

    void deleteAll(List<Order> orders);

    Long getTotalPrice(Order order);
}
