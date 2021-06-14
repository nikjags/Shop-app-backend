package ru.study.shop.adapters.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.study.shop.adapters.controllers.utils.dto_validation.custom_constraints.NotEmptyObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotEmptyObject
public class StockDto {
    @NotNull
    @Min(1)
    private Long productId;

    @NotNull
    @Size(max = 10)
    private String size;

    @NotNull
    @Min(0)
    private Long quantity;
}
