package ru.study.shop.services.interfaces;

import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;

import java.util.List;
import java.util.Optional;

public interface StockService {

    void deleteStock(Stock stock);

    List<Stock> findAll();

    Optional<Stock> findByProductIdAndSize(Long productId, String size);

    List<Stock> findByProduct(Product product);

    List<Stock> findEmptyStocks();

    List<Stock> findBySize(String size);

    Stock saveStock(Stock stock);
}
