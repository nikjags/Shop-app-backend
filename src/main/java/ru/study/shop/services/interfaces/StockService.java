package ru.study.shop.services.interfaces;

import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;

import java.util.List;
import java.util.Optional;

public interface StockService {

    List<Stock> findAll();

    Optional<Stock> findByStockId(Long stockId);

    List<Stock> findByProductId(Long productId);

    List<Stock> findBySize(String size);

    Optional<Stock> findByProductIdAndSize(Long productId, String size);

    List<Stock> findByProduct(Product product);

    List<Stock> findEmptyStocks();

    Stock saveStock(Stock stock);

    void deleteStock(Stock stock);

    void deleteAllStocks(List<Stock> stocks);
}
