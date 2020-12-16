package ru.study.shop.services.impl;

import org.assertj.core.util.Lists;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.easymock.annotation.Mock;
import ru.study.shop.adapters.hibernate.ProductRepository;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.ProductService;

import java.util.List;
import java.util.stream.Collectors;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.unitils.easymock.EasyMockUnitils.replay;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class ProductServiceImplTest {

    private static final List<Product> PRODUCT_LIST = Lists.list(
        new Product("Продукт 1", "Тип 1", "Материал 1", "Производитель 1", "-", 1500L),
        new Product("Продукт 2", "Тип 2", "Материал 1", "Производитель 2", "-", 2000L),
        new Product("Продукт 3", "Тип 2", "Материал 1", "Производитель 2", "-", 3500L),
        new Product("Продукт 4", "Тип 3", "Материал 2", "Производитель 3", "-", 4050L),
        new Product("Продукт 5", "Тип 3", "Материал 3", "Производитель 3", "-", 6000L),
        new Product("Продукт 6", "Тип 3", "Материал 3", "Производитель 3", "-", 7155L),
        new Product("Продукт 7", "Тип 4", "Материал 5", "Производитель 4", "-", 8000L)
    );
    private static final String PRODUCT_NAME_REPRESENTED_IN_LIST = "Продукт 1";
    private static final String PRODUCT_NAME_NOT_REPRESENTED_IN_LIST = "Имя продукта не в списке";
    private static final String PRODUCT_TYPE_REPRESENTED_IN_LIST_ONCE = "Тип 1";
    private static final String PRODUCT_TYPE_REPRESENTED_IN_LIST_SEVERAL_TIMES = "Тип 3";
    private static final String PRODUCT_TYPE_NOT_REPRESENTED_IN_LIST = "Тип продукта не в списке";
    private static final String PRODUCT_MATERIAL_REPRESENTED_IN_LIST_ONCE = "Материал 2";
    private static final String PRODUCT_MATERIAL_REPRESENTED_IN_LIST_SEVERAL_TIMES = "Материал 1";
    private static final String PRODUCT_MATERIAL_NOT_REPRESENTED_IN_LIST = "Материал продукта не в списке";
    private static final String PRODUCT_MANUFACTURER_REPRESENTED_IN_LIST_ONCE = "Материал 2";
    private static final String PRODUCT_MANUFACTURER_REPRESENTED_IN_LIST_SEVERAL_TIMES = "Материал 1";
    private static final String PRODUCT_MANUFACTURER_NOT_REPRESENTED_IN_LIST = "Материал продукта не в списке";

    @Mock
    private ProductRepository productRepository;

    @TestSubject
    ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void findByNameEmptyProductList() {
        expect(productRepository.findAll()).andReturn(Lists.emptyList());
        replay();

        List<Product> result = productService.findByName(PRODUCT_NAME_REPRESENTED_IN_LIST);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByNameNullName() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByName(null);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByNameNoSuchProductNameInList() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByName(PRODUCT_NAME_NOT_REPRESENTED_IN_LIST);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByNameProductNameInList() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByName(PRODUCT_NAME_REPRESENTED_IN_LIST);

        assertEquals(PRODUCT_LIST.stream().
                filter(product -> product.getProductName().equals(PRODUCT_NAME_REPRESENTED_IN_LIST))
                .collect(Collectors.toList()),
            result);
    }

    @Test
    public void findByTypeEmptyProductList() {
        expect(productRepository.findAll()).andReturn(Lists.emptyList());
        replay();

        List<Product> result = productService.findByType(PRODUCT_NAME_REPRESENTED_IN_LIST);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByTypeNullType() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByType(null);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByTypeNoSuchProductTypeInList() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByType(PRODUCT_TYPE_NOT_REPRESENTED_IN_LIST);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByTypeOneProductWithTheTypeInList() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByType(PRODUCT_TYPE_REPRESENTED_IN_LIST_ONCE);

        assertEquals(PRODUCT_LIST.stream()
                .filter(product -> product.getProductType().equals(PRODUCT_TYPE_REPRESENTED_IN_LIST_ONCE))
                .collect(Collectors.toList()),
            result);
    }

    @Test
    public void findByTypeSeveralProductsWithTheTypeInList() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByType(PRODUCT_TYPE_REPRESENTED_IN_LIST_SEVERAL_TIMES);

        assertEquals(PRODUCT_LIST.stream()
                .filter(product -> product.getProductType().equals(PRODUCT_TYPE_REPRESENTED_IN_LIST_SEVERAL_TIMES))
                .collect(Collectors.toList()),
            result);
    }

    @Test
    public void findByMaterialNullMaterial() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByMaterial(null);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByMaterialEmptyProductList() {
        expect(productRepository.findAll()).andReturn(Lists.emptyList());
        replay();

        List<Product> result = productService.findByMaterial(PRODUCT_MATERIAL_NOT_REPRESENTED_IN_LIST);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByMaterialNoSuchMaterial() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByMaterial(PRODUCT_MATERIAL_NOT_REPRESENTED_IN_LIST);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByMaterialRepresentedInListOnce() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByMaterial(PRODUCT_MATERIAL_REPRESENTED_IN_LIST_ONCE);

        assertEquals(PRODUCT_LIST.stream()
                .filter(product -> product.getMaterial().equals(PRODUCT_MATERIAL_REPRESENTED_IN_LIST_ONCE))
                .collect(Collectors.toList()),
            result);
    }

    @Test
    public void findByMaterialRepresentedInListSeveralTimes() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByMaterial(PRODUCT_MATERIAL_REPRESENTED_IN_LIST_SEVERAL_TIMES);

        assertEquals(PRODUCT_LIST.stream()
                .filter(product -> product.getMaterial().equals(PRODUCT_MATERIAL_REPRESENTED_IN_LIST_SEVERAL_TIMES))
                .collect(Collectors.toList()),
            result);
    }

    @Test
    public void findByManufacturerNullManufacturer() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByManufacturer(null);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByManufacturerEmptyProductList() {
        expect(productRepository.findAll()).andReturn(Lists.emptyList());
        replay();

        List<Product> result = productService.findByManufacturer(PRODUCT_MANUFACTURER_NOT_REPRESENTED_IN_LIST);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByManufacturerNoSuchProductManufacturer() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByManufacturer(PRODUCT_MANUFACTURER_NOT_REPRESENTED_IN_LIST);

        assertEquals(Lists.emptyList(), result);
    }

    @Test
    public void findByManufacturerRepresentedInListOnce() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByManufacturer(PRODUCT_MANUFACTURER_REPRESENTED_IN_LIST_ONCE);

        assertEquals(PRODUCT_LIST.stream()
                .filter(product -> product.getManufacturer().equals(PRODUCT_MANUFACTURER_REPRESENTED_IN_LIST_ONCE))
                .collect(Collectors.toList()),
            result);
    }

    @Test
    public void findByManufacturerRepresentedInListSeveralTimes() {
        expect(productRepository.findAll()).andReturn(PRODUCT_LIST);
        replay();

        List<Product> result = productService.findByManufacturer(PRODUCT_MANUFACTURER_REPRESENTED_IN_LIST_SEVERAL_TIMES);

        assertEquals(PRODUCT_LIST.stream()
                .filter(product -> product.getManufacturer().equals(PRODUCT_MANUFACTURER_REPRESENTED_IN_LIST_SEVERAL_TIMES))
                .collect(Collectors.toList()),
            result);
    }
}