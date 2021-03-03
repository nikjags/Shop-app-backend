package ru.study.shop.adapters.hibernate.impl.query_classes.constraint_classes;

import java.util.Collections;
import java.util.List;

public class Constraint<T> {
    private final ConstraintType constraintType;

    private List<T> constraintList = Collections.emptyList();

    private T from;
    private T to;

    private Constraint(List<T> constraintList) {
        this.constraintType = ConstraintType.LIST;
        this.constraintList = constraintList;
    }

    private Constraint(T fromValue, T toValue) {
        this.constraintType = ConstraintType.RANGE;
        this.from = fromValue;
        this.to = toValue;
    }

    public static <E extends Comparable<E>> Constraint<E> getRangeConstraint(E fromBound, E toBound) {
        return new Constraint<>(fromBound, toBound);
    }

    public static <E> Constraint<E> getConstraintList(List<E> constraintList) {
        return new Constraint<>(constraintList);
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public List<T> getConstraintList() {
        return constraintList;
    }

    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }
}
