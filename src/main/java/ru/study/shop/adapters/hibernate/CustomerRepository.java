package ru.study.shop.adapters.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.study.shop.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value =
        "SELECT * " +
        "FROM survey AS s " +
        "WHERE CURRENT_TIMESTAMP BETWEEN s.start_date AND s.end_date;", nativeQuery = true)
    boolean getActiveSurveys();
}
