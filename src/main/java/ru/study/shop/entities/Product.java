package ru.study.shop.entities;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "PRODUCT")
public class Product implements Serializable {
    private static final long serialVersionUID = 878054394L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NaturalId
    private String productName;

    @NaturalId
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
