package ru.study.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.study.shop.adapters.hibernate.CustomerRepository;
import ru.study.shop.entities.Customer;
import ru.study.shop.services.interfaces.CustomerService;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
}
