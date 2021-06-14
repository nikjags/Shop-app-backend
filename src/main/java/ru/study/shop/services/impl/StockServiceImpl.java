package ru.study.shop.services.impl;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.study.shop.adapters.hibernate.StockRepository;
import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;
import ru.study.shop.services.interfaces.StockService;

import java.util.List;
import java.util.Optional;

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
    public Optional<Stock> findByStockId(@NonNull Long stockId) {
        return stockRepository.findById(stockId);
    }

    @Override
    public List<Stock> findByProduct(@NonNull Product product) {
        return stockRepository.findByProduct(product);
    }

    @Override
    public List<Stock> findByProductId(@NonNull Long productId) {
        return stockRepository.findByProductId(productId);
    }

    @Override
    public List<Stock> findBySize(@NonNull String size) {
        return stockRepository.findBySize(size);
    }

    @Override
    public Optional<Stock> findByProductIdAndSize(@NonNull Long productId, @NonNull String size) {
        return stockRepository.findByProductIdAndSize(productId, size);
    }

    @Override
    public List<Stock> findEmptyStocks() {
        return stockRepository.findEmptyStocks();
    }

    @Override
    public Stock saveStock(@NonNull Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public void deleteStock(Stock stock) {
        stockRepository.delete(stock);
    }

    @Override
    public void deleteAllStocks(List<Stock> stocks) {
        stockRepository.deleteAll(stocks);
    }
}
