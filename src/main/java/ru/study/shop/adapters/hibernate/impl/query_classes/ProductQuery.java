package ru.study.shop.adapters.hibernate.impl.query_classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductQuery {
    public static final Long MIN_ID = 1L;
    public static final Long MAX_ID = Long.MAX_VALUE;
    public static final Long MIN_PRICE = 0L;
    public static final Long MAX_PRICE = Long.MAX_VALUE;
    private Optional<Long> fromId = Optional.empty();
    private Optional<Long> toId = Optional.empty();
    private List<String> productNameList = new ArrayList<>();
    private List<String> productTypeList = new ArrayList<>();
    private List<String> productMaterialList = new ArrayList<>();
    private List<String> productManufacturerList = new ArrayList<>();
    private Optional<Long> fromPrice = Optional.empty();
    private Optional<Long> toPrice = Optional.empty();

    private ProductQuery() {
    }

    public static Builder startQuery() {
        return new ProductQuery().new Builder();
    }

    public List<String> getProductNameList() {
        return productNameList;
    }

    public List<String> getProductTypeList() {
        return productTypeList;
    }

    public List<String> getProductMaterialList() {
        return productMaterialList;
    }

    public List<String> getProductManufacturerList() {
        return productManufacturerList;
    }

    public Optional<Long> getLowerIdBound() {
        return fromId;
    }

    public Optional<Long> getUpperIdBound() {
        return toId;
    }

    public Optional<Long> getLowerPriceBound() {
        return fromPrice;
    }

    public Optional<Long> getUpperPriceBound() {
        return toPrice;
    }

    public class Builder {
        private Builder() {
        }

        public Builder idRange(Long fromId, Long toId) {
            if (Objects.nonNull(fromId)) {
                ProductQuery.this.fromId = Optional.of(fromId);
            }
            if (Objects.nonNull(toId)) {
                ProductQuery.this.toId = Optional.of(toId);
            }

            return this;
        }

        public Builder inNameList(List<String> productNameList) {
            ProductQuery.this.productNameList = productNameList;

            return this;
        }

        public Builder inTypeList(List<String> productTypeList) {
            ProductQuery.this.productTypeList = productTypeList;

            return this;
        }

        public Builder inMaterialList(List<String> productMaterialList) {
            ProductQuery.this.productMaterialList = productMaterialList;

            return this;
        }

        public Builder inManufacturerList(List<String> productManufacturerList) {
            ProductQuery.this.productManufacturerList = productManufacturerList;

            return this;
        }

        public Builder priceRange(Long fromPrice, Long toPrice) {
            if (Objects.nonNull(fromPrice)) {
                ProductQuery.this.fromPrice = Optional.of(fromPrice);
            }
            if (Objects.nonNull(toPrice)) {
                ProductQuery.this.toPrice = Optional.of(toPrice);
            }

            return this;
        }

        public ProductQuery build() {
            return ProductQuery.this;
        }
    }
}
