package ru.study.shop.services.interfaces;

import ru.study.shop.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAll();

    Optional<Customer> findById(Long id);
}
