package ru.study.shop.adapters.hibernate.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.study.shop.ShopApplication;
import ru.study.shop.adapters.hibernate.ProductRepository;
import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQueryConstraints;
import ru.study.shop.entities.Product;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
public class CustomProductRepositoryIT {

    private static final Random randomGenerator = new Random();

    private static final int MIN_PRICE = 499;
    private static final int MAX_PRICE = 9999;
    private static final int PRICE_DELTA = 1000;

    private static final int MIN_ID = 1;
    private static final int MAX_ID = 12;
    private static final int ID_DELTA = 5;

    private static final int MAX_LIST_SIZE = 5;

    @Autowired
    private ProductRepository repository;

    @Test
    public void findByProductQueryNullQueryConstraintReturnsLikeFindAll() {
        List<Product> productList;
        try {
            productList = repository.findByProductQueryConstraints(null);
        } catch (NullPointerException npe) {
            fail("Null test for findByProductQuery() method is failed");
            return;
        }
        compareProductLists(repository.findAll(), productList);
    }

    @Test
    public void findByProductQueryEmptyQueryConstraintReturnsLikeFindAll() {
        List<Product> resultList = repository.findByProductQueryConstraints(ProductQueryConstraints.getConstraintsBuilder().build());

        compareProductLists(repository.findAll(), resultList);
    }

    @Test
    public void findByProductQueryWithIdConstraint() {
        Long rightIdBound = getRandomId();
        Long leftIdBound = rightIdBound - ID_DELTA;
        ProductQueryConstraints queryConstraints = ProductQueryConstraints.getConstraintsBuilder()
            .idConstraint(leftIdBound, rightIdBound).build();

        List<Product> resultList = repository.findByProductQueryConstraints(queryConstraints);

        resultList.forEach(product ->
            assertTrue(isInclusiveBetween(product.getId(), leftIdBound, rightIdBound)));
    }

    @Test
    public void findByProductQueryWithPriceConstraint() {
        Long rightPriceBound = getRandomPrice();
        Long leftPriceBound = rightPriceBound - PRICE_DELTA;
        ProductQueryConstraints queryConstraints = ProductQueryConstraints.getConstraintsBuilder()
            .priceConstraint(leftPriceBound, rightPriceBound).build();

        List<Product> resultList = repository.findByProductQueryConstraints(queryConstraints);

        resultList.forEach(product ->
            assertTrue(isInclusiveBetween(product.getPrice(), leftPriceBound, rightPriceBound)));
    }

    @Test
    public void findByProductQueryWithNameConstraint() {
        List<String> nameList = getRandomStringListFrom(getDistinctSortedProductNamesListFromDB()).stream()
            .sorted().collect(Collectors.toList());

        ProductQueryConstraints queryConstraints = ProductQueryConstraints.getConstraintsBuilder()
            .nameConstraint(nameList).build();

        List<String> nameResultList = repository.findByProductQueryConstraints(queryConstraints).stream()
            .map(Product::getProductName).distinct().sorted().collect(Collectors.toList());

        assertEquals(nameList, nameResultList);
    }

    @Test
    public void findByProductQueryWithTypeConstraint() {
        List<String> typeList = getRandomStringListFrom(getDistinctSortedProductTypesListFromDB()).stream()
            .sorted().collect(Collectors.toList());

        ProductQueryConstraints queryConstraints = ProductQueryConstraints.getConstraintsBuilder()
            .typeConstraint(typeList).build();

        List<String> typeResultList = repository.findByProductQueryConstraints(queryConstraints).stream()
            .map(Product::getProductType).distinct().sorted().collect(Collectors.toList());

        assertEquals(typeList, typeResultList);
    }

    @Test
    public void findByProductQueryWithMaterialConstraint() {
        List<String> materialList = getRandomStringListFrom(getDistinctSortedProductMaterialsListFromDB()).stream()
            .sorted().collect(Collectors.toList());

        ProductQueryConstraints queryConstraints = ProductQueryConstraints.getConstraintsBuilder()
            .materialConstraint(materialList).build();

        List<String> manufacturerList = repository.findByProductQueryConstraints(queryConstraints).stream()
            .map(Product::getMaterial).distinct().sorted().collect(Collectors.toList());

        assertEquals(materialList, manufacturerList);
    }

    @Test
    public void findByProductQueryWithManufacturerConstraint() {
        List<String> manufacturerList = getRandomStringListFrom(getDistinctSortedProductManufacturersListFromDB()).stream()
            .sorted().collect(Collectors.toList());

        ProductQueryConstraints queryConstraints = ProductQueryConstraints.getConstraintsBuilder()
            .manufacturerConstraint(manufacturerList).build();

        List<String> manufacturerResultList = repository.findByProductQueryConstraints(queryConstraints).stream()
            .map(Product::getManufacturer).distinct().sorted().collect(Collectors.toList());

        assertEquals(manufacturerList, manufacturerResultList);
    }

    /////////////////////////////////////////////////////////////
    // Impl
    /////////////////////////////////////////////////////////////

    private List<String> getDistinctSortedProductNamesListFromDB() {
        return repository.findAll().stream()
            .map(Product::getProductName).sorted().distinct().collect(Collectors.toList());
    }

    private List<String> getDistinctSortedProductTypesListFromDB() {
        return repository.findAll().stream()
            .map(Product::getProductType).sorted().distinct().collect(Collectors.toList());
    }

    private List<String> getDistinctSortedProductMaterialsListFromDB() {
        return repository.findAll().stream()
            .map(Product::getMaterial).sorted().distinct().collect(Collectors.toList());
    }

    private List<String> getDistinctSortedProductManufacturersListFromDB() {
        return repository.findAll().stream()
            .map(Product::getManufacturer).sorted().distinct().collect(Collectors.toList());
    }

    private List<String> getRandomStringListFrom(List<String> valuesList) {
        List<String> nameList = new ArrayList<>();

        for (int i = 0; i < MAX_LIST_SIZE; i++) {
            nameList.add(valuesList.get(
                randomGenerator.nextInt(valuesList.size() - 1)));
        }

        return nameList.stream().distinct().collect(Collectors.toList());
    }

    private Long getRandomId() {
        return (long) randomGenerator.nextInt(MAX_ID - MIN_ID) + MIN_ID;
    }

    private Long getRandomPrice() {
        return (long) randomGenerator.nextInt(MAX_PRICE - MIN_PRICE) + MIN_PRICE;
    }

    private <E extends Comparable<E>> boolean isInclusiveBetween(E value, E leftBound, E rightBound) {
        return value.compareTo(leftBound) >= 0 && value.compareTo(rightBound) <= 0;
    }

    private void compareProductLists(List<Product> expectedList, List<Product> actualList) {
        if (expectedList.size() != actualList.size()) {
            fail("Different list size compared to list returned by findAll()");
        }

        expectedList.sort(Comparator.comparing(Product::getId));
        actualList.sort(Comparator.comparing(Product::getId));

        Iterator<Product> queryIterator = expectedList.iterator();
        Iterator<Product> findAllIterator = actualList.iterator();

        while (queryIterator.hasNext()) {
            assertTrue(compareProductsByValue(queryIterator.next(), findAllIterator.next()));
        }
    }

    private boolean compareProductsByValue(Product product1, Product product2) {
        if (isNull(product1) || isNull(product2)) {
            return false;
        }

        return (product1.getId().equals(product2.getId()))
            && (product1.getProductName().equals(product2.getProductName()))
            && (product1.getProductType().equals(product2.getProductType()))
            && (product1.getManufacturer().equals(product2.getManufacturer()))
            && (product1.getMaterial().equals(product2.getMaterial()))
            && (product1.getDescription().equals(product2.getDescription()))
            && (product1.getPrice().equals(product2.getPrice()));
    }
}