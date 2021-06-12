package ru.study.shop.adapters.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull
    private Long customerId;

    @NotNull
    @Size(min = 1)
    private Map<Long, Integer> productIdAmountMap;

    @NotNull
    private LocalDateTime orderedTime;

    @NotNull
    private boolean delivered;
}