package ru.study.shop.adapters.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.study.shop.adapters.controllers.utils.dto_validation.custom_constraints.NotEmptyObject;
import ru.study.shop.adapters.controllers.utils.dto_validation.groups.OnCreate;
import ru.study.shop.adapters.controllers.utils.dto_validation.groups.OnUpdate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotEmptyObject(groups = {OnCreate.class, OnUpdate.class})
public class OrderDto {
    @NotNull(groups = {OnCreate.class})
    @Min(value = 1, groups = {OnCreate.class, OnUpdate.class})
    private Long customerId;

    @NotNull(groups = {OnCreate.class})
    @Size(min = 1, groups = {OnCreate.class, OnUpdate.class})
    private Map<
        @NotNull @Min(value = 1, groups = {OnCreate.class, OnUpdate.class}) Long,
        @NotNull @Min(value = 1, groups = {OnCreate.class, OnUpdate.class}) Integer> productIdAmountMap;

    @NotNull(groups = {OnCreate.class})
    private LocalDateTime orderedTime;

    @NotNull(groups = {OnCreate.class})
    private Boolean delivered;
}
