package ru.study.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.study.shop.adapters.hibernate.StockRepository;
import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;
import ru.study.shop.services.interfaces.StockService;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    @Override
    public List<Stock> findByProduct(Product product) {
        return stockRepository.findByProduct(product);
    }

    @Override
    public List<Stock> findByProductId(Long productId) {
        if (isNull(productId)) {
            return findAll();
        }

        return stockRepository.findByProductId(productId);
    }

    @Override
    public List<Stock> findBySize(String size) {
        if (isNull(size)) {
            return findAll();
        }

        return stockRepository.findBySize(size);
    }

    @Override
    public Optional<Stock> findByProductIdAndSize(Long productId, String size) {
        return stockRepository.findByProductIdAndSize(productId, size);
    }

    @Override
    public List<Stock> findEmptyStocks() {
        return stockRepository.findEmptyStocks();
    }

    @Override
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public void deleteStock(Stock stock) {
        stockRepository.delete(stock);
    }
}
