package ru.study.shop.adapters.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.study.shop.adapters.hibernate.StockRepository;
import ru.study.shop.entities.Stock;

import java.util.List;


@Controller
@RequestMapping("/shop/products")
public class ProductController {

    private StockRepository stockRepository;

    public ProductController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping("/stocks")
    public List<Stock> getAllSupplies() {
        return stockRepository.findAll();
    }
}
