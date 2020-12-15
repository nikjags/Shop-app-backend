package ru.study.shop.adapters.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.study.shop.entities.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
