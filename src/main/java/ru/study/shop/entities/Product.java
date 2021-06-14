package ru.study.shop.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String productType;
    private String material;
    private String manufacturer;
    private String description;
    private Long price;

    public Product() {
    }

    public Product(
        String productName,
        String productType,
        String material,
        String manufacturer,
        String description,
        Long price) {
        this.productName = productName;
        this.productType = productType;
        this.material = material;
        this.manufacturer = manufacturer;
        this.description = description;
        this.price = price;
    }
}
