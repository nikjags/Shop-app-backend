
package ru.study.shop.services.interfaces;

import ru.study.shop.entities.Order;
import ru.study.shop.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    List<Product> findByName(String name);

    List<Product> findByType(String type);

    List<Product> findByManufacturer(String manufacturer);

    List<Product> findFromPriceToPrice(Long fromPrice, Long toPrice);

    Product saveProduct(Product product);

    List<Product> saveAll(List<Product> products);

    void deleteProduct(Product product);

    void deleteAll(List<Product> products);
}
