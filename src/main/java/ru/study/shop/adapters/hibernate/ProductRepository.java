package ru.study.shop.adapters.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.study.shop.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
