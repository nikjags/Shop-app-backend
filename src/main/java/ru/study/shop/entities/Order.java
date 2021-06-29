package ru.study.shop.entities;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"orderId", "customerId", "products", "orderedTime", "delivered"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("orderId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("customerId")
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "orders_product",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonProperty(value = "products")
    private List<Product> productList = new ArrayList<>();

    private LocalDateTime orderedTime;

    private boolean delivered;

    public Order(Customer customer, List<Product> productList, LocalDateTime orderedTime, boolean delivered) {
        this.customer = customer;
        this.productList = productList;
        this.orderedTime = orderedTime;
        this.delivered = delivered;
    }
}
