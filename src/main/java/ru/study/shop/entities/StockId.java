package ru.study.shop.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class StockId implements Serializable {
    private Product product;

    private String size;

    public StockId() {
    }

    public StockId(Product product, String size) {
        this.product = product;
        this.size = size;
    }
}
