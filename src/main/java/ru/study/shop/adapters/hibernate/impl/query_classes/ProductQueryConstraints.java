package ru.study.shop.adapters.hibernate.impl.query_classes;

import ru.study.shop.adapters.hibernate.impl.query_classes.constraint_classes.Constraint;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.study.shop.adapters.hibernate.impl.query_classes.constraint_classes.Constraint.getConstraintList;
import static ru.study.shop.adapters.hibernate.impl.query_classes.constraint_classes.Constraint.getRangeConstraint;

public class ProductQueryConstraints {
    public static final Long MIN_ID = 1L;
    public static final Long MAX_ID = Long.MAX_VALUE;
    public static final Long MIN_PRICE = 0L;
    public static final Long MAX_PRICE = Long.MAX_VALUE;

    private static final Constraint<String> emptyConstraintList = getConstraintList(Collections.emptyList());

    private Constraint<Long> idConstraint = getRangeConstraint(MIN_ID, MAX_ID);
    private boolean idIsConstrained = false;

    private Constraint<String> nameConstraint = emptyConstraintList;
    private boolean nameIsConstrained = false;

    private Constraint<String> typeConstraint = emptyConstraintList;
    private boolean typeIsConstrained = false;

    private Constraint<String> materialConstraint = emptyConstraintList;
    private boolean materialIsConstrained = false;

    private Constraint<String> manufacturerConstraint = emptyConstraintList;
    private boolean manufacturerIsConstrained = false;

    private Constraint<Long> priceConstraint = getRangeConstraint(MIN_PRICE, MAX_PRICE);
    private boolean priceIsConstrained = false;

    private ProductQueryConstraints() {
    }

    public static Builder getConstraintsBuilder() {
        return new ProductQueryConstraints().new Builder();
    }

    public boolean isIdIsConstrained() {
        return idIsConstrained;
    }

    public boolean isNameIsConstrained() {
        return nameIsConstrained;
    }

    public boolean isTypeIsConstrained() {
        return typeIsConstrained;
    }

    public boolean isMaterialIsConstrained() {
        return materialIsConstrained;
    }

    public boolean isManufacturerIsConstrained() {
        return manufacturerIsConstrained;
    }

    public boolean isPriceIsConstrained() {
        return priceIsConstrained;
    }

    public Constraint<Long> getIdConstraint() {
        return idConstraint;
    }

    public Constraint<String> getNameConstraint() {
        return nameConstraint;
    }

    public Constraint<String> getTypeConstraint() {
        return typeConstraint;
    }

    public Constraint<String> getMaterialConstraint() {
        return materialConstraint;
    }

    public Constraint<String> getManufacturerConstraint() {
        return manufacturerConstraint;
    }

    public Constraint<Long> getPriceConstraint() {
        return priceConstraint;
    }

    public class Builder {
        private Builder() {
        }

        public Builder idConstraint(Long fromId, Long toId) {
            if (isNull(fromId) && isNull(toId)) {
                return this;
            }

            Long from = MIN_ID;
            if (nonNull(fromId)) {
                from = fromId;
            }

            Long to = MAX_ID;
            if (nonNull(toId)) {
                to = toId;
            }

            if (from > to) {
                return this;
            }

            ProductQueryConstraints.this.idConstraint = getRangeConstraint(from, to);
            ProductQueryConstraints.this.idIsConstrained = true;
            return this;
        }

        public Builder idConstraint(List<Long> idList) {
            if (isNull(idList)) {
                return this;
            }

            idList.removeIf(Objects::isNull);

            if (idList.isEmpty()) {
                return this;
            }

            idList = idList.stream().distinct().collect(Collectors.toList());

            ProductQueryConstraints.this.idConstraint = getConstraintList(idList);
            ProductQueryConstraints.this.idIsConstrained = true;
            return this;
        }

        public Builder nameConstraint(List<String> nameList) {
            if (isNull(nameList)) {
                return this;
            }

            nameList.removeIf(Objects::isNull);

            if (nameList.isEmpty()) {
                return this;
            }

            nameList = nameList.stream().distinct().collect(Collectors.toList());

            ProductQueryConstraints.this.nameConstraint = getConstraintList(nameList);
            ProductQueryConstraints.this.nameIsConstrained = true;
            return this;
        }

        public Builder typeConstraint(List<String> typeList) {
            if (isNull(typeList)) {
                return this;
            }

            typeList.removeIf(Objects::isNull);

            if (typeList.isEmpty()) {
                return this;
            }

            typeList = typeList.stream().distinct().collect(Collectors.toList());

            ProductQueryConstraints.this.typeConstraint = getConstraintList(typeList);
            ProductQueryConstraints.this.typeIsConstrained = true;
            return this;
        }

        public Builder materialConstraint(List<String> materialList) {
            if (isNull(materialList)) {
                return this;
            }

            materialList.removeIf(Objects::isNull);

            if (materialList.isEmpty()) {
                return this;
            }

            materialList = materialList.stream().distinct().collect(Collectors.toList());

            ProductQueryConstraints.this.materialConstraint = getConstraintList(materialList);
            ProductQueryConstraints.this.materialIsConstrained = true;
            return this;
        }

        public Builder manufacturerConstraint(List<String> manufacturerList) {
            if (isNull(manufacturerList)) {
                return this;
            }

            manufacturerList.removeIf(Objects::isNull);

            if (manufacturerList.isEmpty()) {
                return this;
            }

            manufacturerList = manufacturerList.stream().distinct().collect(Collectors.toList());

            ProductQueryConstraints.this.manufacturerConstraint = getConstraintList(manufacturerList);
            ProductQueryConstraints.this.manufacturerIsConstrained = true;
            return this;
        }

        public Builder priceConstraint(Long fromPrice, Long toPrice) {
            if (isNull(fromPrice) && isNull(toPrice)) {
                return this;
            }

            Long from = MIN_PRICE;
            if (nonNull(fromPrice)) {
                from = fromPrice;
            }

            Long to = MAX_PRICE;
            if (nonNull(toPrice)) {
                to = toPrice;
            }

            if (from > to) {
                return this;
            }

            ProductQueryConstraints.this.priceConstraint = getRangeConstraint(from, to);
            ProductQueryConstraints.this.priceIsConstrained = true;
            return this;
        }

        public Builder priceConstraint(List<Long> priceList) {
            if (isNull(priceList)) {
                return this;
            }

            priceList.removeIf(Objects::isNull);

            if (priceList.isEmpty()) {
                return this;
            }

            priceList = priceList.stream().distinct().collect(Collectors.toList());

            ProductQueryConstraints.this.priceConstraint = getConstraintList(priceList);
            ProductQueryConstraints.this.priceIsConstrained = true;
            return this;
        }

        public ProductQueryConstraints build() {
            return ProductQueryConstraints.this;
        }
    }
}
