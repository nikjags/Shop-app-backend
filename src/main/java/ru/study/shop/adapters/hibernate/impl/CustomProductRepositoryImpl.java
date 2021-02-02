package ru.study.shop.adapters.hibernate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.study.shop.adapters.hibernate.CustomProductRepository;
import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQuery;
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

public class CustomProductRepositoryImpl implements CustomProductRepository {

    private static final String ID_COLUMN = "id";
    private static final String PRODUCT_NAME_COLUMN = "productName";
    private static final String PRODUCT_TYPE_COLUMN = "productType";
    private static final String MANUFACTURER_COLUMN = "manufacturer";
    private static final String MATERIAL_COLUMN = "material";
    private static final String PRICE_COLUMN = "price";

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findByProductQuery(ProductQuery productQuery) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (productQuery.getLowerIdBound().isPresent() || productQuery.getUpperIdBound().isPresent()) {
            predicates.add(getIdRangePredicate(cb, root, productQuery));
        }

        if (!productQuery.getProductNameList().isEmpty()) {
            predicates.add(getInListPredicate(cb, root, productQuery.getProductNameList(), PRODUCT_NAME_COLUMN));
        }

        if (!productQuery.getProductTypeList().isEmpty()) {
            predicates.add(getInListPredicate(cb, root, productQuery.getProductTypeList(), PRODUCT_TYPE_COLUMN));
        }

        if (!productQuery.getProductManufacturerList().isEmpty()) {
            predicates.add(getInListPredicate(cb, root, productQuery.getProductManufacturerList(), MANUFACTURER_COLUMN));
        }

        if (!productQuery.getProductMaterialList().isEmpty()) {
            predicates.add(getInListPredicate(cb, root, productQuery.getProductMaterialList(), MATERIAL_COLUMN));
        }

        if (productQuery.getLowerPriceBound().isPresent() || productQuery.getUpperPriceBound().isPresent()) {
            predicates.add(getPriceRangePredicate(cb, root, productQuery));
        }

        query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    /////////////////////////////////////////////////////////////
    // Impl
    /////////////////////////////////////////////////////////////

    private Predicate getIdRangePredicate(CriteriaBuilder cb, Root<Product> root, ProductQuery productQuery) {
        Long lowerIdBound = ProductQuery.MIN_ID;
        Long upperIdBound = ProductQuery.MAX_ID;
        if (productQuery.getLowerIdBound().isPresent()) {
            lowerIdBound = productQuery.getLowerIdBound().get();
        }
        if (productQuery.getUpperIdBound().isPresent()) {
            upperIdBound = productQuery.getUpperIdBound().get();
        }

        return cb.between(root.get(ID_COLUMN),
            lowerIdBound,
            upperIdBound);
    }

    private Predicate getInListPredicate(CriteriaBuilder cb, Root<Product> root, List<String> valuesList, String entityFieldName) {
        In<String> inListClause = cb.in(root.get(entityFieldName));

        valuesList.forEach(inListClause::value);
        return inListClause;
    }

    private Predicate getPriceRangePredicate(CriteriaBuilder cb, Root<Product> root, ProductQuery productQuery) {
        Long lowerPriceBound = ProductQuery.MIN_PRICE;
        Long upperPriceBound = ProductQuery.MAX_PRICE;
        if (productQuery.getLowerPriceBound().isPresent()) {
            lowerPriceBound = productQuery.getLowerPriceBound().get();
        }
        if (productQuery.getUpperPriceBound().isPresent()) {
            upperPriceBound = productQuery.getUpperPriceBound().get();
        }

        return cb.between(root.get(PRICE_COLUMN),
            lowerPriceBound,
            upperPriceBound);
    }
}
