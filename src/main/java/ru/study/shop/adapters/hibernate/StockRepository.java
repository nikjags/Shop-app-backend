package ru.study.shop.adapters.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;
import ru.study.shop.entities.StockId;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, StockId> {
    Optional<Stock> findByProductIdAndSize(Long productId, String size);

    List<Stock> findByProduct(Product product);

    List<Stock> findByProductId(Long productId);

    List<Stock> findBySize(String size);

    @Query(
        "SELECT s " +
        "FROM Stock s " +
        "WHERE s.quantity = 0")
    List<Stock> findEmptyStocks();

    @Query(
        "SELECT s " +
        "FROM Stock s " +
        "WHERE s.quantity <> 0")
    List<Stock> findNonEmptyStocks();
}
