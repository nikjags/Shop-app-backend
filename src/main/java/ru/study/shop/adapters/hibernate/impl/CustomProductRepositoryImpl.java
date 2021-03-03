package ru.study.shop.adapters.hibernate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.study.shop.adapters.hibernate.CustomProductRepository;
import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQueryConstraints;
import ru.study.shop.adapters.hibernate.impl.query_classes.constraint_classes.Constraint;
import ru.study.shop.entities.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static ru.study.shop.adapters.hibernate.impl.query_classes.constraint_classes.ConstraintType.RANGE;

public class CustomProductRepositoryImpl implements CustomProductRepository {
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "productName";
    private static final String TYPE_COLUMN = "productType";
    private static final String MANUFACTURER_COLUMN = "manufacturer";
    private static final String MATERIAL_COLUMN = "material";
    private static final String PRICE_COLUMN = "price";

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findByProductQueryConstraints(ProductQueryConstraints productQueryConstraints) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        if (isNull(productQueryConstraints)) {
            query.select(root);
            return entityManager.createQuery(query).getResultList();
        }

        List<Predicate> predicates = new ArrayList<>();

        if (productQueryConstraints.isIdIsConstrained()) {
            if (productQueryConstraints.getIdConstraint().getConstraintType() == RANGE) {
                predicates.add(getRangePredicate(cb, root, ID_COLUMN, productQueryConstraints.getIdConstraint()));
            } else {
                predicates.add(getInListPredicate(cb, root, productQueryConstraints.getIdConstraint().getConstraintList(), ID_COLUMN));
            }
        }

        if (productQueryConstraints.isNameIsConstrained()) {
            predicates.add(getInListPredicate(cb, root, productQueryConstraints.getNameConstraint().getConstraintList(), NAME_COLUMN));
        }

        if (productQueryConstraints.isTypeIsConstrained()) {
            predicates.add(getInListPredicate(cb, root, productQueryConstraints.getTypeConstraint().getConstraintList(), TYPE_COLUMN));
        }

        if (productQueryConstraints.isManufacturerIsConstrained()) {
            predicates.add(getInListPredicate(cb, root, productQueryConstraints.getManufacturerConstraint().getConstraintList(), MANUFACTURER_COLUMN));
        }

        if (productQueryConstraints.isMaterialIsConstrained()) {
            predicates.add(getInListPredicate(cb, root, productQueryConstraints.getMaterialConstraint().getConstraintList(), MATERIAL_COLUMN));
        }

        if (productQueryConstraints.isPriceIsConstrained()) {
            if (productQueryConstraints.getPriceConstraint().getConstraintType() == RANGE) {
                predicates.add(getRangePredicate(cb, root, PRICE_COLUMN, productQueryConstraints.getPriceConstraint()));
            } else {
                predicates.add(getInListPredicate(cb, root, productQueryConstraints.getPriceConstraint().getConstraintList(), PRICE_COLUMN));
            }
        }

        query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    /////////////////////////////////////////////////////////////
    // Impl
    /////////////////////////////////////////////////////////////

    private <E extends Comparable<E>> Predicate getRangePredicate(CriteriaBuilder cb, Root<Product> root, String columnName, Constraint<E> constraint) {
        return cb.between(root.get(columnName),
            constraint.getFrom(),
            constraint.getTo());
    }

    private <T> Predicate getInListPredicate(CriteriaBuilder cb, Root<Product> root, List<T> valuesList, String entityFieldName) {
        In<T> inListClause = cb.in(root.get(entityFieldName));

        valuesList.forEach(inListClause::value);
        return inListClause;
    }
}
