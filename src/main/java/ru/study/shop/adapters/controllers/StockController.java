package ru.study.shop.adapters.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.study.shop.entities.Stock;
import ru.study.shop.services.interfaces.StockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop/stocks")
public class StockController {

    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping()
    public ResponseEntity<List<Stock>> findAll(
        @RequestParam(value = "size", required = false) String size,
        @RequestParam(value = "productId", required = false) Long productId) {

        boolean sizeIsNull = Objects.isNull(size);
        boolean productIdIsNull = Objects.isNull(productId);

        if (sizeIsNull && productIdIsNull) {
            return ResponseEntity.ok(stockService.findAll());
        }

        if (sizeIsNull) {
            return ResponseEntity.ok(
                stockService.findAll().stream()
                    .filter(stock -> stock.getProduct().getId().equals(productId))
                    .collect(Collectors.toList()));
        }

        if (productIdIsNull) {
            return ResponseEntity.ok(
                stockService.findAll().stream()
                    .filter(stock -> stock.getSize().equals(size))
                    .collect(Collectors.toList()));
        }

        List<Stock> stockList = new ArrayList<>();

        Optional<Stock> stock = stockService.findByProductIdAndSize(productId, size);
        stock.ifPresent(stockList::add);
        return ResponseEntity.ok(stockList);
    }

    @GetMapping("/{productId}/{size}")
    public ResponseEntity<Stock> findByProductIdAndSize(
        @PathVariable("productId") Long productId,
        @PathVariable("size") String size) {

        return stockService.findByProductIdAndSize(productId, size)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
}
