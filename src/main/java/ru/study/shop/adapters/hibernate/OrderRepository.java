package ru.study.shop.adapters.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.study.shop.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
