package ru.study.shop.services.impl;

import org.springframework.stereotype.Service;
import ru.study.shop.adapters.hibernate.ProductRepository;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findAll().stream()
            .filter(product -> product.getProductName().equals(name))
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByType(String type) {
        return productRepository.findAll().stream()
            .filter(product -> product.getProductType().equals(type))
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByMaterial(String material) {
        return productRepository.findAll().stream()
            .filter(product -> product.getMaterial().equals(material))
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByManufacturer(String manufacturer) {
        return productRepository.findAll().stream()
            .filter(product -> product.getManufacturer().equals(manufacturer))
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> findFromPriceToPrice(Long fromPrice, Long toPrice) {
        if (fromPrice.compareTo(toPrice) < 0)
            return new ArrayList<>();
        return productRepository.findAll().stream()
            .filter(product ->
                product.getPrice().compareTo(fromPrice) >= 0 &&
                    product.getPrice().compareTo(toPrice) <= 0)
            .collect(Collectors.toList());
    }

    @Override
    public Product saveProduct(Product product) {
        return null;
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteAll(List<Product> products) {
        productRepository.deleteAll(products);
    }
}
