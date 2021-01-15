package ru.study.shop.entities;

import javax.persistence.*;

@Entity
@Table(name = "STOCK")
@IdClass(StockId.class)
public class Stock {
    /* TODO
     *
     * FethType.EAGER для соединения с Product не очень круто, хочется LAZY,
     * но при попытке получить объекты Stock через репозиторий выскакивает ошибка при попытке
     * сериализовать хайбернейтовский прокси-класс для Product.
     * Разобраться.
     *
     * Существует вероятность, что это происходит из-за составного ключа.
     * Так как StockId -- сериализуемый класс, то начинаются проблемесы.
     *
     * Можно, конечно, отказаться от составного ключа и использовать суррогатный...
     *
     */
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
