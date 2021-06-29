package ru.study.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
