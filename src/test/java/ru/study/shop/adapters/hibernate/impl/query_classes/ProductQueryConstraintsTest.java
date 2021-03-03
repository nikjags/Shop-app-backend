package ru.study.shop.adapters.hibernate.impl.query_classes;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;
import static ru.study.shop.adapters.hibernate.impl.query_classes.ProductQueryConstraints.*;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class ProductQueryConstraintsTest {
    private static final String STRING_FILLER = "string";

    private static final Random randomGenerator = new Random();
    private static final int NUMBER_OF_STRINGS_BOUND_IN_RANDOM_LIST = 20;
    private static final int STRING_LENGTH_BOUND_IN_RANDOM_LIST = 20;
    private static final int LONG_LIST_SIZE_BOUND = 50;
    private static final int DELTA = 5;

    @Test
    public void idRangeConstraintBothBoundsIsNullReturnsNoConstraint() {
        ProductQueryConstraints testQuery = getConstraintsBuilder()
            .idConstraint(null, null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(MIN_ID, testQuery.getIdConstraint().getFrom());
        assertEquals(MAX_ID, testQuery.getIdConstraint().getTo());
    }

    @Test
    public void idRangeConstraintLeftBoundIsNullReturnsConstraintWithMinIdAsLeftValue() {
        Long randomId = getPositiveLongValue();
        ProductQueryConstraints testQuery = getConstraintsBuilder()
            .idConstraint(null, randomId).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.ID)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(MIN_ID, testQuery.getIdConstraint().getFrom());
        assertEquals(randomId, testQuery.getIdConstraint().getTo());
    }

    @Test
    public void idRangeConstraintRightBoundIsNullReturnsConstraintWithMaxIdAsRightValue() {
        Long randomId = getPositiveLongValue();
        ProductQueryConstraints testQuery = getConstraintsBuilder()
            .idConstraint(randomId, null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.ID)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(randomId, testQuery.getIdConstraint().getFrom());
        assertEquals(MAX_ID, testQuery.getIdConstraint().getTo());
    }

    @Test
    public void idRangeConstraintBothBoundsIsPresentReturnsConstraint() {
        Long leftIdBound = getPositiveLongValue();
        Long rightIdBound = leftIdBound + DELTA;
        ProductQueryConstraints testQuery = getConstraintsBuilder()
            .idConstraint(leftIdBound, rightIdBound).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.ID)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(leftIdBound, testQuery.getIdConstraint().getFrom());
        assertEquals(rightIdBound, testQuery.getIdConstraint().getTo());
    }

    @Test
    public void idRangeConstraintLeftBoundIsGreaterThanRightBoundReturnsNoConstraint() {
        Long randomValue = getPositiveLongValue();
        ProductQueryConstraints actualQuery = getConstraintsBuilder()
            .idConstraint(randomValue + DELTA, randomValue).build();

        assertFalse(actualQuery.isIdIsConstrained());
    }

    @Test
    public void idConstraintNullListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .idConstraint(null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getIdConstraint().getConstraintList());
    }

    @Test
    public void idConstraintEmptyListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .idConstraint(emptyList()).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getIdConstraint().getConstraintList());
    }

    @Test
    public void idConstraintOnlyNullLongsInListReturnsNoConstraint() {
        List<Long> listWithOneNullString = new ArrayList<>();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .idConstraint(listWithOneNullString).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getIdConstraint().getConstraintList());
    }

    @Test
    public void idConstraintOneNullLongInListIgnoreNullValueAndReturnsConstraint() {
        List<Long> listWithOneNullLong = getLongList();
        listWithOneNullLong.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .idConstraint(listWithOneNullLong).build();

        listWithOneNullLong.removeIf(Objects::isNull);

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.ID)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(listWithOneNullLong, testQuery.getIdConstraint().getConstraintList());
    }

    @Test
    public void idConstraintFilledListCheckReturnsConstraint() {
        List<Long> randomLongList = getLongList();
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .idConstraint(randomLongList).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.ID)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(randomLongList, testQuery.getIdConstraint().getConstraintList());
    }

    @Test
    public void priceRangeConstraintBothBoundsIsNullReturnsNoConstraint() {
        ProductQueryConstraints testQuery = getConstraintsBuilder()
            .priceConstraint(null, null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(MIN_PRICE, testQuery.getPriceConstraint().getFrom());
        assertEquals(MAX_PRICE, testQuery.getPriceConstraint().getTo());
    }

    @Test
    public void priceRangeConstraintLeftBoundIsNullReturnsConstraintWithMinPriceAsLeftValue() {
        Long randomPrice = getPositiveLongValue();
        ProductQueryConstraints testQuery = getConstraintsBuilder()
            .priceConstraint(null, randomPrice).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.PRICE)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(MIN_PRICE, testQuery.getPriceConstraint().getFrom());
        assertEquals(randomPrice, testQuery.getPriceConstraint().getTo());
    }

    @Test
    public void priceRangeConstraintRightBoundIsNullReturnsConstraintWithMaxPriceAsRightValue() {
        Long randomPrice = getPositiveLongValue();
        ProductQueryConstraints testQuery = getConstraintsBuilder()
            .priceConstraint(randomPrice, null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.PRICE)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(randomPrice, testQuery.getPriceConstraint().getFrom());
        assertEquals(MAX_PRICE, testQuery.getPriceConstraint().getTo());
    }

    @Test
    public void priceRangeConstraintBothBoundsIsPresentReturnsConstraint() {
        Long leftPriceBound = getPositiveLongValue();
        Long rightPriceBound = leftPriceBound + DELTA;
        ProductQueryConstraints testQuery = getConstraintsBuilder()
            .idConstraint(leftPriceBound, rightPriceBound).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.ID)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(leftPriceBound, testQuery.getIdConstraint().getFrom());
        assertEquals(rightPriceBound, testQuery.getIdConstraint().getTo());
    }

    @Test
    public void priceRangeConstraintLeftBoundIsGreaterThanRightBoundReturnsNoConstraint() {
        Long randomValue = getPositiveLongValue();
        ProductQueryConstraints actualQuery = getConstraintsBuilder()
            .priceConstraint(randomValue + 1, randomValue).build();

        assertFalse(actualQuery.isPriceIsConstrained());
    }

    @Test
    public void priceConstraintNullListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .priceConstraint(null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getPriceConstraint().getConstraintList());
    }

    @Test
    public void priceConstraintEmptyListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .priceConstraint(emptyList()).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getPriceConstraint().getConstraintList());
    }

    @Test
    public void priceConstraintOnlyNullLongsInListReturnsNoConstraint() {
        List<Long> listWithOneNullString = new ArrayList<>();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .priceConstraint(listWithOneNullString).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getPriceConstraint().getConstraintList());
    }

    @Test
    public void priceConstraintOneNullLongInListIgnoreNullValueAndReturnsConstraint() {
        List<Long> listWithOneNullLong = getLongList();
        listWithOneNullLong.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .priceConstraint(listWithOneNullLong).build();

        listWithOneNullLong.removeIf(Objects::isNull);

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.PRICE)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(listWithOneNullLong, testQuery.getPriceConstraint().getConstraintList());
    }

    @Test
    public void priceConstraintFilledListCheckReturnsConstraint() {
        List<Long> randomLongList = getLongList();
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .priceConstraint(randomLongList).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.PRICE)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(randomLongList, testQuery.getPriceConstraint().getConstraintList());
    }

    @Test
    public void nameConstraintNullListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .nameConstraint(null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getNameConstraint().getConstraintList());
    }

    @Test
    public void nameConstraintEmptyListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .nameConstraint(emptyList()).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getNameConstraint().getConstraintList());
    }

    @Test
    public void nameConstraintOnlyNullStringsInListReturnsNoConstraint() {
        List<String> listWithOneNullString = new ArrayList<>();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .nameConstraint(listWithOneNullString).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getNameConstraint().getConstraintList());
    }

    @Test
    public void nameConstraintOneNullStringInListIgnoreNullValueAndReturnsConstraint() {
        List<String> listWithOneNullString = getStringList();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .nameConstraint(listWithOneNullString).build();

        listWithOneNullString.removeIf(Objects::isNull);

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.NAME)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(listWithOneNullString, testQuery.getNameConstraint().getConstraintList());
    }

    @Test
    public void nameConstraintFilledListCheckReturnsConstraint() {
        List<String> randomStringList = getStringList();
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .nameConstraint(randomStringList).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.NAME)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(randomStringList, testQuery.getNameConstraint().getConstraintList());
    }

    @Test
    public void typeConstraintNullListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .typeConstraint(null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getTypeConstraint().getConstraintList());
    }

    @Test
    public void typeConstraintEmptyListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .typeConstraint(emptyList()).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getTypeConstraint().getConstraintList());
    }

    @Test
    public void typeConstraintOnlyNullStringsInListReturnsNoConstraint() {
        List<String> listWithOneNullString = new ArrayList<>();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .typeConstraint(listWithOneNullString).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getTypeConstraint().getConstraintList());
    }

    @Test
    public void typeConstraintOneNullStringInListIgnoreNullValueAndReturnsConstraint() {
        List<String> listWithOneNullString = getStringList();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .typeConstraint(listWithOneNullString).build();

        listWithOneNullString.removeIf(Objects::isNull);

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.TYPE)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(listWithOneNullString, testQuery.getTypeConstraint().getConstraintList());
    }

    @Test
    public void typeConstraintFilledListCheckReturnsConstraint() {
        List<String> randomStringList = getStringList();
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .typeConstraint(randomStringList).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.TYPE)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(randomStringList, testQuery.getTypeConstraint().getConstraintList());
    }

    @Test
    public void materialConstraintNullListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .materialConstraint(null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getMaterialConstraint().getConstraintList());
    }

    @Test
    public void materialConstraintEmptyListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .materialConstraint(emptyList()).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getMaterialConstraint().getConstraintList());
    }

    @Test
    public void materialConstraintOnlyNullStringsInListReturnsNoConstraint() {
        List<String> listWithOneNullString = new ArrayList<>();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .materialConstraint(listWithOneNullString).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getMaterialConstraint().getConstraintList());
    }

    @Test
    public void materialConstraintOneNullStringInListIgnoreNullValueAndReturnsConstraint() {
        List<String> listWithOneNullString = getStringList();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .materialConstraint(listWithOneNullString).build();

        listWithOneNullString.removeIf(Objects::isNull);

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.MATERIAL)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(listWithOneNullString, testQuery.getMaterialConstraint().getConstraintList());
    }

    @Test
    public void materialConstraintFilledListCheckReturnsConstraint() {
        List<String> randomStringList = getStringList();
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .materialConstraint(randomStringList).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.MATERIAL)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(randomStringList, testQuery.getMaterialConstraint().getConstraintList());
    }

    @Test
    public void manufacturerConstraintNullListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .manufacturerConstraint(null).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getManufacturerConstraint().getConstraintList());
    }

    @Test
    public void manufacturerConstraintEmptyListReturnsNoConstraint() {
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .manufacturerConstraint(emptyList()).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getManufacturerConstraint().getConstraintList());
    }

    @Test
    public void manufacturerConstraintOnlyNullStringsInListReturnsNoConstraint() {
        List<String> listWithOneNullString = new ArrayList<>();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .manufacturerConstraint(listWithOneNullString).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> assertFalse(isConstrained));
        assertEquals(emptyList(), testQuery.getManufacturerConstraint().getConstraintList());
    }

    @Test
    public void manufacturerConstraintOneNullStringInListIgnoreNullValueAndReturnsConstraint() {
        List<String> listWithOneNullString = getStringList();
        listWithOneNullString.add(null);

        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .manufacturerConstraint(listWithOneNullString).build();

        listWithOneNullString.removeIf(Objects::isNull);

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.MANUFACTURER)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(listWithOneNullString, testQuery.getManufacturerConstraint().getConstraintList());
    }

    @Test
    public void manufacturerConstraintFilledListCheckReturnsConstraint() {
        List<String> randomStringList = getStringList();
        ProductQueryConstraints testQuery = ProductQueryConstraints.getConstraintsBuilder()
            .manufacturerConstraint(randomStringList).build();

        getQueryConstraintMap(testQuery)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (productField.equals(ProductField.MANUFACTURER)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });
        assertEquals(randomStringList, testQuery.getManufacturerConstraint().getConstraintList());
    }

    @Test
    public void severalFieldConstraintsCheck() {
        getAllProductFieldCombinations().forEach(this::checkFieldConstraintCombination);
    }

    public void checkFieldConstraintCombination(Set<ProductField> fieldSet) {
        ProductQueryConstraints.Builder queryBuilder = ProductQueryConstraints.getConstraintsBuilder();

        Long idLeftBound = getPositiveLongValue();
        Long idRightBound = idLeftBound + DELTA;
        if (fieldSet.contains(ProductField.ID)) {
            queryBuilder.idConstraint(idLeftBound, idRightBound);
        }

        Long priceLeftBound = getPositiveLongValue();
        Long priceRightBound = priceLeftBound + DELTA;
        if (fieldSet.contains(ProductField.PRICE)) {
            queryBuilder.priceConstraint(priceLeftBound, priceRightBound);
        }

        List<String> nameList = getStringList();
        if (fieldSet.contains(ProductField.NAME)) {
            queryBuilder.nameConstraint(nameList);
        }

        List<String> typeList = getStringList();
        if (fieldSet.contains(ProductField.TYPE)) {
            queryBuilder.typeConstraint(typeList);
        }

        List<String> materialList = getStringList();
        if (fieldSet.contains(ProductField.MATERIAL)) {
            queryBuilder.materialConstraint(materialList);
        }

        List<String> manufacturerList = getStringList();
        if (fieldSet.contains(ProductField.MANUFACTURER)) {
            queryBuilder.manufacturerConstraint(manufacturerList);
        }

        ProductQueryConstraints query = queryBuilder.build();

        getQueryConstraintMap(query)
            .forEach((ProductField productField, Boolean isConstrained) -> {
                if (fieldSet.contains(productField)) {
                    assertTrue(isConstrained);
                } else {
                    assertFalse(isConstrained);
                }
            });

        if (fieldSet.contains(ProductField.ID)) {
            assertEquals(idLeftBound, query.getIdConstraint().getFrom());
            assertEquals(idRightBound, query.getIdConstraint().getTo());
        }

        if (fieldSet.contains(ProductField.PRICE)) {
            assertEquals(priceLeftBound, query.getPriceConstraint().getFrom());
            assertEquals(priceRightBound, query.getPriceConstraint().getTo());
        }

        if (fieldSet.contains(ProductField.NAME)) {
            assertEquals(nameList, query.getNameConstraint().getConstraintList());
        }

        if (fieldSet.contains(ProductField.TYPE)) {
            assertEquals(typeList, query.getTypeConstraint().getConstraintList());
        }

        if (fieldSet.contains(ProductField.MATERIAL)) {
            assertEquals(materialList, query.getMaterialConstraint().getConstraintList());
        }

        if (fieldSet.contains(ProductField.MANUFACTURER)) {
            assertEquals(manufacturerList, query.getManufacturerConstraint().getConstraintList());
        }
    }

    private Long getPositiveLongValue() {
        return Math.abs(randomGenerator.nextLong()) + 1;
    }

    /////////////////////////////////////////////////////////////
    // Impl
    /////////////////////////////////////////////////////////////

    /**
     * Generates an random size <code>ArrayList<String></code> with a unique random length strings.
     * Size of a list is varies from 1 to <code>NUMBER_OF_STRINGS_BOUND_IN_RANDOM_LIST</code>.
     * String length varies from 0 to <code>STRING_LENGTH_BOUND_IN_RANDOM_LIST</code>.
     *
     * @return random size <code>ArrayList<String></code> with a random length strings.
     */
    private List<String> getStringList() {
        List<String> stringList = new ArrayList<>();

        int stringNumber = randomGenerator.nextInt(NUMBER_OF_STRINGS_BOUND_IN_RANDOM_LIST) + 1;

        for (int i = 0; i < stringNumber; i++) {
            Integer stringLength = randomGenerator.nextInt(STRING_LENGTH_BOUND_IN_RANDOM_LIST + 1);
            Integer lengthOfStringLength = stringLength.toString().length();

            if (stringLength == 0) {
                stringList.add("");
                continue;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < (stringLength - lengthOfStringLength) / STRING_FILLER.length(); j++) {
                stringBuilder.append(STRING_FILLER);
            }

            int j = 0;
            while (stringBuilder.length() != stringLength - lengthOfStringLength) {
                if (j == STRING_FILLER.length() - 1) {
                    j = 0;
                }

                stringBuilder.append(STRING_FILLER.charAt(j));

                j++;
            }

            stringBuilder.append(stringLength);

            stringList.add(stringBuilder.toString());
        }
        return stringList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Generates an random size <code>ArrayList<Long></code> with a unique random positive long values.
     * Size of a list is varies from 1 to <code>LONG_LIST_SIZE_BOUND</code>.
     *
     * @return random size <code>ArrayList<String></code> with a random positive long values.
     */
    private List<Long> getLongList() {
        List<Long> longList = new ArrayList<>();

        int longNumber = randomGenerator.nextInt(LONG_LIST_SIZE_BOUND) + 1;

        for (int i = 0; i < longNumber; i++) {
            longList.add(getPositiveLongValue());
        }

        return longList.stream().distinct().collect(Collectors.toList());
    }

    private Map<ProductField, Boolean> getQueryConstraintMap(ProductQueryConstraints query) {
        Map<ProductField, Boolean> constraintMap = new HashMap<>();
        constraintMap.put(ProductField.ID, query.isIdIsConstrained());
        constraintMap.put(ProductField.NAME, query.isNameIsConstrained());
        constraintMap.put(ProductField.TYPE, query.isTypeIsConstrained());
        constraintMap.put(ProductField.MATERIAL, query.isMaterialIsConstrained());
        constraintMap.put(ProductField.MANUFACTURER, query.isManufacturerIsConstrained());
        constraintMap.put(ProductField.PRICE, query.isPriceIsConstrained());

        return constraintMap;
    }

    /**
     * Generates every ProductField enum combination.
     *
     * @return Set of combinations sets.
     */
    private Set<Set<ProductField>> getAllProductFieldCombinations() {
        Set<Set<ProductField>> combinationsSet = new HashSet<>();
        ProductField[] productFieldArray = ProductField.values();

        for (int i = 2; i <= productFieldArray.length; i++) {
            Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(productFieldArray.length, i);
            while (iterator.hasNext()) {
                int[] combination = iterator.next();
                Set<ProductField> currentCombinationSet = EnumSet.noneOf(ProductField.class); // empty EnumSet

                for (int productFieldIndex : combination) {
                    currentCombinationSet.add(productFieldArray[productFieldIndex]);
                }
                combinationsSet.add(currentCombinationSet);
            }
        }

        return combinationsSet;
    }

    private enum ProductField {
        ID, NAME, TYPE, MATERIAL, MANUFACTURER, PRICE
    }
}