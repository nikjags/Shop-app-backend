package ru.study.shop.entities;

import java.io.Serializable;
import java.util.Objects;

public class StockId implements Serializable {
    private Product product;

    private String size;

    public StockId() {
    }

    public StockId(Product product, String size) {
        this.product = product;
        this.size = size;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockId stockId = (StockId) o;
        return product.equals(stockId.product) &&
            size.equals(stockId.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, size);
    }
}
