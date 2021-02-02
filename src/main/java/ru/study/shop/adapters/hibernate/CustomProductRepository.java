package ru.study.shop.adapters.hibernate;

import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQuery;
import ru.study.shop.entities.Product;

import java.util.List;

public interface CustomProductRepository {
    List<Product> findByProductQuery(ProductQuery productQuery);
}
