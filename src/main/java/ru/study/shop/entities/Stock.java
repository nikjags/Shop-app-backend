package ru.study.shop.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "STOCK")
@IdClass(StockId.class)
public class Stock {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "product_id")
    private Product product;

    @Id
    private String size;

    private Long quantity;

    public Stock() {

    }

    public Stock(Product product, String size, Long quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }
}
