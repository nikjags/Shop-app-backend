package ru.study.shop.adapters.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.study.shop.entities.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    LocalDateTime MIN_ORDER_DATE_TIME = LocalDateTime.MIN;
    LocalDateTime MAX_ORDER_DATE_TIME = LocalDateTime.MAX;

    @Query(value =
            "SELECT * " +
            "FROM orders " +
            "WHERE ordered_time >= ?1 ",
        nativeQuery = true
    )
    List<Order> findAllFromDate(@Param("fromDate") LocalDateTime fromDate);

    @Query(value =
        "SELECT * " +
            "FROM orders " +
            "WHERE ordered_time <= ?1 ",
        nativeQuery = true
    )
    List<Order> findAllToDate(@Param("fromDate") LocalDateTime toDate);

    @Query(value =
            "SELECT * " +
            "FROM orders " +
            "WHERE ordered_time BETWEEN ?1 AND ?2 ",
        nativeQuery = true
    )
    List<Order> findAllFromDateToDate(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}
