package ru.study.shop.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class StockId implements Serializable {
    private static final long serialVersionUID = 878054395L;

    private Product product;

    private String size;

    public StockId() {
    }

    public StockId(Product product, String size) {
        this.product = product;
        this.size = size;
    }
}
