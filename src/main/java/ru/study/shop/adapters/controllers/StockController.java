package ru.study.shop.adapters.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.study.shop.entities.Stock;
import ru.study.shop.services.interfaces.StockService;

import java.util.List;

@Controller
@RequestMapping("/shop/stock")
public class StockController {

    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping()
    public List<Stock> findAll() {
        return stockService.findAll();
    }
}
