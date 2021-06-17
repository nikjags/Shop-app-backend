package ru.study.shop.adapters.controllers;

import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.easymock.annotation.Mock;
import ru.study.shop.adapters.controllers.dto.ProductDto;
import ru.study.shop.adapters.controllers.rest.ProductController;
import ru.study.shop.entities.Order;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.unitils.easymock.EasyMockUnitils.replay;
import static org.unitils.easymock.EasyMockUnitils.verify;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class ProductControllerTest {
    private static final String STRING_VAL = "test";
    private static final Long TEST_PRICE = 7777L;
    private static final int PRODUCT_NAME_MAX_LENGTH = 100;
    private static final int PRODUCT_TYPE_MAX_LENGTH = 100;
    private static final int PRODUCT_MATERIAL_MAX_LENGTH = 100;
    private static final int PRODUCT_MANUFACTURER_MAX_LENGTH = 100;
    private static final int PRODUCT_DESCRIPTION_MAX_LENGTH = 1000;
    private static final Long VALID_PRODUCT_ID = 1L;

    private static final Random randomGenerator = new Random();

    @Mock
    private ProductService productService;

    @TestSubject
    private ProductController productController;

    @Before
    public void setUp() {
        productController = new ProductController(productService);
    }

    @Test
    public void getProductByIdWithInvalidId() {
        Long nullIdIsInvalid = null;
        Long zeroIdIsInvalid = 0L;
        Long negativeIdIsInvalid = -5L;

        assertThrows(ResponseStatusException.class, () -> productController.getProductById(nullIdIsInvalid));
        assertThrows(ResponseStatusException.class, () -> productController.getProductById(zeroIdIsInvalid));
        assertThrows(ResponseStatusException.class, () -> productController.getProductById(negativeIdIsInvalid));
    }

    @Test
    public void createProductWithEmptyProductDto() {
        ProductDto emptyProductDto = new ProductDto();
        assertThrows(ResponseStatusException.class, () -> productController.createProduct(emptyProductDto));
    }

    @Test
    public void createProductWithNonFullProductDto() {
        List<ProductDto> nonFullyDTOs = new ArrayList<>();
        nonFullyDTOs.add(new ProductDto());
        nonFullyDTOs.add(new ProductDto(null, "test", "test", "test", "test", TEST_PRICE));
        nonFullyDTOs.add(new ProductDto("test", null, "test", "test", "test", TEST_PRICE));
        nonFullyDTOs.add(new ProductDto("test", "test", null, "test", "test", TEST_PRICE));
        nonFullyDTOs.add(new ProductDto("test", "test", "test", null, "test", TEST_PRICE));

        for (ProductDto productDto : nonFullyDTOs) {
            assertThrows(ResponseStatusException.class, () -> productController.createProduct(productDto));
        }
    }

    @Test
    public void createProductWithNoPriceInProductDto() {
        ProductDto dtoWithoutPrice = getValidDto();
        dtoWithoutPrice.setPrice(null);

        Product expectedProduct = new Product(
            dtoWithoutPrice.getProductName(),
            dtoWithoutPrice.getProductName(),
            dtoWithoutPrice.getMaterial(),
            dtoWithoutPrice.getManufacturer(),
            dtoWithoutPrice.getDescription(),
            null);

        expect(productService.saveProduct(expectedProduct)).andReturn(expectedProduct).once();
        replay();

        productController.createProduct(dtoWithoutPrice);

        verify();
    }

    @Test
    public void createProductWithNoDescriptionInProductDto() {
        ProductDto dtoWithoutDescription = getValidDto();
        dtoWithoutDescription.setDescription(null);

        Product expectedProduct = new Product(
            dtoWithoutDescription.getProductName(),
            dtoWithoutDescription.getProductName(),
            dtoWithoutDescription.getMaterial(),
            dtoWithoutDescription.getManufacturer(),
            null,
            dtoWithoutDescription.getPrice());

        expect(productService.saveProduct(expectedProduct)).andReturn(expectedProduct).once();
        replay();

        productController.createProduct(dtoWithoutDescription);

        verify();
    }

    @Test
    public void createProductWithInvalidProductName() {
        ProductDto dtoWithInvalidProductName = getValidDto();
        dtoWithInvalidProductName.setProductName(generateString(PRODUCT_NAME_MAX_LENGTH + 1));

        assertThrows(ResponseStatusException.class, () -> productController.createProduct(dtoWithInvalidProductName));
    }

    @Test
    public void createProductWithInvalidProductType() {
        ProductDto dtoWithInvalidProductType = getValidDto();
        dtoWithInvalidProductType.setProductType(generateString(PRODUCT_TYPE_MAX_LENGTH + 1));

        assertThrows(ResponseStatusException.class, () -> productController.createProduct(dtoWithInvalidProductType));
    }

    @Test
    public void createProductWithInvalidProductManufacturer() {
        ProductDto dtoWithInvalidProductManufacturer = getValidDto();
        dtoWithInvalidProductManufacturer.setManufacturer(generateString(PRODUCT_MANUFACTURER_MAX_LENGTH + 1));

        assertThrows(ResponseStatusException.class, () -> productController.createProduct(dtoWithInvalidProductManufacturer));
    }

    @Test
    public void createProductWithInvalidProductMaterial() {
        ProductDto dtoWithInvalidProductMaterial = getValidDto();
        dtoWithInvalidProductMaterial.setMaterial(generateString(PRODUCT_MATERIAL_MAX_LENGTH + 1));

        assertThrows(ResponseStatusException.class, () -> productController.createProduct(dtoWithInvalidProductMaterial));
    }

    @Test
    public void createProductWithInvalidProductDescription() {
        ProductDto dtoWithInvalidProductDescription = getValidDto();
        dtoWithInvalidProductDescription.setDescription(generateString(PRODUCT_DESCRIPTION_MAX_LENGTH + 1));

        assertThrows(ResponseStatusException.class, () -> productController.createProduct(dtoWithInvalidProductDescription));
    }

    @Test
    public void createProductWithInvalidProductPrice() {
        ProductDto dtoWithNegativePrice = getValidDto();
        dtoWithNegativePrice.setPrice(-1L);

        ProductDto dtoWithBigPrice = getValidDto();
        dtoWithBigPrice.setPrice(Long.MAX_VALUE);

        assertThrows(ResponseStatusException.class, () -> productController.createProduct(dtoWithNegativePrice));
    }

    @Test
    public void editProductWithInvalidId() {
        ProductDto validDto = getValidDto();
        Long negativeIdIsInvalid = -1L;
        Long zeroIdIsInvalid = -1L;

        assertThrows(ResponseStatusException.class, () -> productController.editProduct(negativeIdIsInvalid, validDto));
        assertThrows(ResponseStatusException.class, () -> productController.editProduct(zeroIdIsInvalid, validDto));
    }

    @Test
    public void editProductWithNullDto() {
        assertThrows(ResponseStatusException.class, () -> productController.editProduct(VALID_PRODUCT_ID, null));
    }

    @Test
    public void editProductWithEmptyDto() {
        ProductDto emptyDto = new ProductDto();
        assertThrows(ResponseStatusException.class, () -> productController.editProduct(VALID_PRODUCT_ID, emptyDto));
    }

    @Test
    public void deleteProductWithInvalidId() {
        Long negativeIdIsInvalid = -1L;
        Long zeroIdIsInvalid = 0L;

        assertThrows(ResponseStatusException.class, () -> productController.deleteProduct(negativeIdIsInvalid));
        assertThrows(ResponseStatusException.class, () -> productController.deleteProduct(zeroIdIsInvalid));
    }

    @Test
    public void deleteProductIdWithValidAndNonPresentIdThrowsException() {
        Order deletableOrder = new Order();

        expect(productService.findById(VALID_PRODUCT_ID)).andReturn(Optional.empty()).atLeastOnce();
        replay();

        assertThrows(ResponseStatusException.class, () -> productController.deleteProduct(VALID_PRODUCT_ID));
    }

    @Test
    public void deleteProductIdWithValidAndPresentIdReturnsOk() {
        Product deletableProduct = new Product();
        deletableProduct.setId(VALID_PRODUCT_ID);

        expect(productService.findById(VALID_PRODUCT_ID)).andReturn(Optional.of(deletableProduct)).atLeastOnce();
        productService.deleteProduct(deletableProduct);
        expectLastCall();
        replay();

        ResponseEntity<Product> response = productController.deleteProduct(deletableProduct.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private ProductDto getValidDto() {
        return new ProductDto(
            STRING_VAL, STRING_VAL, STRING_VAL,
            STRING_VAL, STRING_VAL, TEST_PRICE
        );
    }

    private String generateString(long length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(randomGenerator.nextInt(128));
        }

        return builder.toString();
    }
}
