package ru.study.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.study.shop.adapters.hibernate.OrderRepository;
import ru.study.shop.entities.Customer;
import ru.study.shop.entities.Order;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> findAllFromDate(LocalDateTime fromDate) {
        System.out.println("IN ALL FROM DATE");
        return orderRepository.findAllFromDate(fromDate);
    }

    @Override
    public List<Order> findAllToDate(LocalDateTime toDate) {
        System.out.println("IN ALL TO DATE");

        return orderRepository.findAllToDate(toDate);
    }

    @Override
    public List<Order> findAllFromToDate(LocalDateTime from, LocalDateTime to) {
        System.out.println("IN ALL FROM TO DATE");
        return orderRepository.findAllFromDateToDate(from, to);
    }

    @Override
    public List<Order> findByCustomer(Customer customer) {
        return orderRepository.findAll()
            .stream()
            .filter(order -> order.getCustomer().equals(customer))
            .collect(Collectors.toList());
    }

    @Override
    public List<Order> saveAll(List<Order> orders) {
        return orderRepository.saveAll(orders);
    }

    @Override
    public List<Order> findByProduct(Product product) {
        return orderRepository.findAll()
            .stream()
            .filter(order -> order.getProductList().contains(product))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteAll(List<Order> orders) {
        orders.forEach(this::deleteOrder);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Long getTotalPrice(Order order) {
        return order.getProductList().stream()
            .map(Product::getPrice)
            .collect(Collectors.toList())
            .stream()
            .reduce((long) 0, (Long::sum));
    }

    @Override
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }
}
