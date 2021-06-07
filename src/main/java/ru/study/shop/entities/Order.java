package ru.study.shop.entities;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
@JsonPropertyOrder({"orderId", "customerId", "products", "orderedTime", "delivered"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("orderId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("customerId")
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "orders_product",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonProperty(value = "products")
    private List<Product> productList = new ArrayList<>();

    private LocalDateTime orderedTime;

    private boolean delivered;

    public Order() {
    }

    public Order(Customer customer, List<Product> productList, LocalDateTime orderedTime, boolean delivered) {
        this.customer = customer;
        this.productList = productList;
        this.orderedTime = orderedTime;
        this.delivered = delivered;
    }
}
