package ru.study.shop.services.interfaces;

import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQueryConstraints;
import ru.study.shop.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> findByName(String name);

    List<Product> findByType(String type);

    List<Product> findByMaterial(String material);

    List<Product> findByManufacturer(String manufacturer);

    List<Product> findFromPriceToPrice(Long fromPrice, Long toPrice);

    List<Product> findByProductQuery(ProductQueryConstraints productQueryConstraints);

    Product saveProduct(Product product);

    List<Product> saveAll(List<Product> products);

    void deleteById(Long id);

    void deleteProduct(Product product);

    void deleteAll(List<Product> products);
}
