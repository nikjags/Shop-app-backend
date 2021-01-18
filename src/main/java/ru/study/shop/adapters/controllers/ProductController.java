package ru.study.shop.adapters.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.ProductService;

import java.util.List;

@Controller
@RequestMapping("/shop/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        return productService.findById(productId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

}
