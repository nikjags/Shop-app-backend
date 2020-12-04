package ru.study.shop.services.interfaces;

import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;

import java.util.List;

public interface StockService {

    void deleteStock(Stock stock);

    List<Stock> findAll();

    List<Stock> findByProduct(Product product);

    List<Stock> findEmptyStocks();

    List<Stock> findBySize(String size);

    Stock saveStock(Stock stock);
}
