package ru.study.shop.entities;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "STOCK")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"productId", "size", "quantity"})
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("productId")
    @NaturalId
    private Product product;

    @NaturalId
    private String size;

    private Long quantity;
}


