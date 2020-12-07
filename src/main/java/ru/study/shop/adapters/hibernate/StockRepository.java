package ru.study.shop.adapters.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.study.shop.entities.Stock;
import ru.study.shop.entities.StockId;

public interface StockRepository extends JpaRepository<Stock, StockId> {
}
