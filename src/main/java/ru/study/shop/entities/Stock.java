package ru.study.shop.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "STOCK")
@IdClass(StockId.class)
public class Stock {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Id
    private String size;

    private Long quantity;

    private Stock() {

    }

    public Stock(Product product, String size, Long quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }
}
