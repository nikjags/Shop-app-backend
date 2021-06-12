package ru.study.shop.adapters.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.study.shop.adapters.controllers.utils.dto_validation.custom_constraints.NotEmptyObject;
import ru.study.shop.adapters.controllers.utils.dto_validation.groups.OnCreate;
import ru.study.shop.adapters.controllers.utils.dto_validation.groups.OnUpdate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NotEmptyObject(groups = {OnCreate.class, OnUpdate.class})
public class ProductDto {
    @NotNull(groups = {OnCreate.class})
    @Length(max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String productName;

    @NotNull(groups = {OnCreate.class})
    @Length(max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String productType;

    @NotNull(groups = {OnCreate.class})
    @Length(max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String material;

    @NotNull(groups = {OnCreate.class})
    @Length(max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String manufacturer;

    @Length(max = 1000, groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Min(value = 0, groups = {OnCreate.class, OnUpdate.class})
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    private Long price;
}
