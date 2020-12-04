package ru.study.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.study.shop.adapters.hibernate.StockRepository;
import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;
import ru.study.shop.services.interfaces.StockService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    private StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> findByProduct(Product product) {
        return stockRepository.findAll()
            .stream()
            .filter(stock -> stock.getProduct().equals(product))
            .collect(Collectors.toList());
    }

    @Override
    public List<Stock> findEmptyStocks() {
        return stockRepository.findAll()
            .stream()
            .filter(stock -> stock.getQuantity() == 0)
            .collect(Collectors.toList());
    }

    @Override
    public List<Stock> findBySize(String size) {
        return stockRepository.findAll()
            .stream()
            .filter(stock -> stock.getSize().equals(size))
            .collect(Collectors.toList());
    }

    @Override
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public void deleteStock(Stock stock) {
        stockRepository.delete(stock);
    }

    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }
}
