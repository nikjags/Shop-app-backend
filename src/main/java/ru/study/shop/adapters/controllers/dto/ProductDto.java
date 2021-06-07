package ru.study.shop.adapters.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @NotNull
    @Length(max = 100)
    private String productName;

    @NotNull
    @Length(max = 100)
    private String productType;

    @NotNull
    @Length(max = 100)
    private String material;

    @NotNull
    @Length(max = 100)
    private String manufacturer;

    @Length(max = 1000)
    private String description;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Long price;
}
