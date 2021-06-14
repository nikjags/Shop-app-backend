package ru.study.shop.adapters.controllers;

import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.easymock.annotation.Mock;
import ru.study.shop.adapters.controllers.dto.StockDto;
import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;
import ru.study.shop.services.interfaces.ProductService;
import ru.study.shop.services.interfaces.StockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.unitils.easymock.EasyMockUnitils.replay;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class StockControllerTest {
    private static final int STOCK_LIST_SIZE = 10;

    private static final Long VALID_STOCK_ID = 1L;
    private static final Long VALID_PRODUCT_ID = 1L;
    private static final String VALID_STOCK_SIZE = "43";
    private static final Long VALID_STOCK_QUANTITY = 5L;

    private static final Long NEGATIVE_ID = -1L;
    private static final Long ZERO_ID = 0L;
    private static final String INVALID_SIZE = "STRING WITH LENGTH MORE THAN 10";
    private static final Long NEGATIVE_QUANTITY = -1L;
    private static final Long ZERO_QUANTITY = 0L;

    @Mock
    StockService stockService;

    @Mock
    ProductService productService;

    @TestSubject
    StockController stockController;

    @Before
    public void setUp() {
        stockController = new StockController(stockService, productService);
    }

    @Test
    public void findAllWithNoParametersCallsServiceFindAllReturnsExpectedList() {
        List<Stock> expectedStockList = getStockList(VALID_PRODUCT_ID, VALID_STOCK_SIZE);

        expect(stockService.findAll()).andReturn(expectedStockList).atLeastOnce();
        replay();

        List<Stock> returnedStockList = stockController.findAll(null, null).getBody();

        assertEquals(expectedStockList, returnedStockList);
    }

    @Test
    public void findAllWithInvalidProductIdThrowsException() {
        assertThrows(ResponseStatusException.class, () -> stockController.findAll(NEGATIVE_ID, null));
        assertThrows(ResponseStatusException.class, () -> stockController.findAll(ZERO_ID, null));
    }

    @Test
    public void findAllWithSizeConstraintCallsFindBySizeReturnsExpectedList() {
        String size = "40";
        List<Stock> expectedStockList = getStockList(VALID_PRODUCT_ID, size);

        expect(stockService.findBySize(size)).andReturn(expectedStockList).atLeastOnce();
        replay();

        List<Stock> returnedStockList = stockController.findAll(null, size).getBody();

        assertEquals(expectedStockList, returnedStockList);
    }

    @Test
    public void findAllWithProductIdConstraintCallsFindByProductIdReturnsExpectedList() {
        Long productId = 2L;
        List<Stock> expectedStockList = getStockList(productId, VALID_STOCK_SIZE);

        expect(stockService.findByProductId(productId)).andReturn(expectedStockList).atLeastOnce();
        replay();

        List<Stock> returnedStockList = stockController.findAll(productId, null).getBody();

        assertEquals(expectedStockList, returnedStockList);
    }

    @Test
    public void findAll_WithProductIdAndSizeConstraint_CallsFindByProductIdAndSize_ReturnsExpectedList() {
        Product product = new Product();
        product.setId(VALID_PRODUCT_ID);
        String size = "40";
        Optional<Stock> expectedOptionalStock = of(new Stock(VALID_STOCK_ID, product, size, VALID_STOCK_QUANTITY));

        expect(stockService.findByProductIdAndSize(product.getId(), size))
            .andReturn(expectedOptionalStock).atLeastOnce();
        replay();

        List<Stock> returnedStockList = stockController.findAll(VALID_PRODUCT_ID, size).getBody();

        Assert.assertNotNull(returnedStockList);
        assertEquals(1, returnedStockList.size());

        assertEquals(expectedOptionalStock.get(), returnedStockList.get(0));
    }

    @Test
    public void findByStockId_WithInvalidId_ThrowsException() {
        assertThrows(ResponseStatusException.class, () -> stockController.findByStockId(NEGATIVE_ID));
        assertThrows(ResponseStatusException.class, () -> stockController.findByStockId(ZERO_ID));
    }

    @Test
    public void findByStockId_NoStockPresent_ThrowsException() {
        expect(stockService.findByStockId(VALID_STOCK_ID))
            .andReturn(Optional.empty()).atLeastOnce();
        replay();

        assertThrows(ResponseStatusException.class, () -> stockController.findByStockId(VALID_STOCK_ID));
    }

    @Test
    public void findByStockId_WithStockPresent_ReturnsOkAndExpectedStock() {
        Stock expectedStock = getValidStock();

        expect(stockService.findByStockId(VALID_STOCK_ID))
            .andReturn(Optional.of(expectedStock)).atLeastOnce();
        replay();

        ResponseEntity<Stock> response = stockController.findByStockId(VALID_STOCK_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStock, response.getBody());
    }

    @Test
    public void findByProductIdAndSize_NullArguments_ThrowsException() {
        assertThrows(ResponseStatusException.class, () -> stockController.findByProductIdAndSize(null, null));
        assertThrows(ResponseStatusException.class, () -> stockController.findByProductIdAndSize(null, VALID_STOCK_SIZE));
        assertThrows(ResponseStatusException.class, () -> stockController.findByProductIdAndSize(VALID_PRODUCT_ID, null));
    }

    @Test
    public void findByProductIdAndSize_WithInvalidId_ThrowsException() {
        assertThrows(ResponseStatusException.class, () -> stockController.findByProductIdAndSize(NEGATIVE_ID, VALID_STOCK_SIZE));
        assertThrows(ResponseStatusException.class, () -> stockController.findByProductIdAndSize(ZERO_ID, VALID_STOCK_SIZE));
    }

    @Test
    public void findByProductIdAndSize_WithValidArgs_GetsEmptyOptional_And_ThrowsException() {
        expect(stockService.findByProductIdAndSize(VALID_PRODUCT_ID, VALID_STOCK_SIZE))
            .andReturn(Optional.empty()).atLeastOnce();
        replay();

        assertThrows(ResponseStatusException.class, () -> stockController.findByProductIdAndSize(VALID_PRODUCT_ID, VALID_STOCK_SIZE));
    }

    @Test
    public void findByProductIdAndSize_WithValidArgs_GetsNonEmptyOptional_AndReturnsExpectedStock() {
        Stock expectedStock = getValidStock();
        Optional<Stock> expectedOptionalStock = of(expectedStock);

        expect(stockService.findByProductIdAndSize(expectedStock.getProduct().getId(), VALID_STOCK_SIZE))
            .andReturn(expectedOptionalStock).atLeastOnce();
        replay();

        Stock returnedStock = stockController.findByProductIdAndSize(VALID_PRODUCT_ID, VALID_STOCK_SIZE).getBody();

        assertEquals(expectedOptionalStock.get(), returnedStock);
    }

    @Test
    public void createStockWithInvalidProductIdThrowsException() {
        StockDto dto = getValidStockDto();

        dto.setProductId(null);
        assertThrows(ResponseStatusException.class, () -> stockController.createStock(dto));

        dto.setProductId(NEGATIVE_ID);
        assertThrows(ResponseStatusException.class, () -> stockController.createStock(dto));

        dto.setProductId(ZERO_ID);
        assertThrows(ResponseStatusException.class, () -> stockController.createStock(dto));
    }

    @Test
    public void createStockWithInvalidSizeThrowsException() {
        StockDto dto = getValidStockDto();

        dto.setSize(INVALID_SIZE);
        assertThrows(ResponseStatusException.class, () -> stockController.createStock(dto));
    }

    @Test
    public void createStockWithInvalidQuantityThrowsException() {
        StockDto dto = getValidStockDto();

        dto.setQuantity(NEGATIVE_QUANTITY);
        assertThrows(ResponseStatusException.class, () -> stockController.createStock(dto));
    }

    @Test
    public void createStockWithValidStockDtoReturnsExpectedStock() {
        StockDto dto = getValidStockDto();
        Stock expectedStock = mapDtoToStock(dto);
        Product product = new Product();
        product.setId(VALID_PRODUCT_ID);

        expect(productService.findById(dto.getProductId()))
            .andReturn(of(product));
        expect(stockService.saveStock(expectedStock)).andReturn(expectedStock).atLeastOnce();
        replay();

        Stock returnedStock = stockController.createStock(dto).getBody();

        assertEquals(expectedStock, returnedStock);
    }

    @Test
    public void editStockWithInvalidProductIdThrowsException() {
        assertThrows(ResponseStatusException.class, () ->
            stockController.editStock(NEGATIVE_ID, VALID_STOCK_SIZE, VALID_STOCK_QUANTITY));
        assertThrows(ResponseStatusException.class, () ->
            stockController.editStock(ZERO_ID, VALID_STOCK_SIZE, VALID_STOCK_QUANTITY));
    }

    @Test
    public void editStockWithInvalidSizeThrowsException() {
        assertThrows(ResponseStatusException.class, () ->
            stockController.editStock(VALID_PRODUCT_ID, INVALID_SIZE, VALID_STOCK_QUANTITY));
    }

    @Test
    public void editStockWithInvalidQuantityThrowsException() {
        assertThrows(ResponseStatusException.class, () ->
            stockController.editStock(VALID_PRODUCT_ID, VALID_STOCK_SIZE, NEGATIVE_QUANTITY));
    }

    @Test
    public void editStockWithNoStockPresentThrowsException() {
        expect(stockService.findByProductIdAndSize(VALID_PRODUCT_ID, VALID_STOCK_SIZE))
            .andReturn(Optional.empty());
        replay();

        assertThrows(ResponseStatusException.class, () ->
            stockController.editStock(VALID_PRODUCT_ID, VALID_STOCK_SIZE, VALID_STOCK_QUANTITY));
    }

    @Test
    public void editStockWithStockPresentReturnsOkWithExpectedBody() {
        Stock expectedStock = getValidStock();

        expect(stockService.findByProductIdAndSize(VALID_PRODUCT_ID, VALID_STOCK_SIZE))
            .andReturn(Optional.of(expectedStock)).atLeastOnce();
        expect(stockService.saveStock(expectedStock)).andReturn(expectedStock).atLeastOnce();
        replay();

        ResponseEntity<Stock> actualResponse = stockController.editStock(VALID_PRODUCT_ID, VALID_STOCK_SIZE, VALID_STOCK_QUANTITY);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedStock, actualResponse.getBody());
    }

    @Test
    public void deleteStockWithInvalidProductIdThrowsException() {
        assertThrows(ResponseStatusException.class, () ->
            stockController.deleteStock(NEGATIVE_ID, VALID_STOCK_SIZE));
        assertThrows(ResponseStatusException.class, () ->
            stockController.deleteStock(ZERO_ID, VALID_STOCK_SIZE));
    }

    @Test
    public void deleteStockWithInvalidStockSizeThrowsException() {
        assertThrows(ResponseStatusException.class, () ->
            stockController.deleteStock(VALID_PRODUCT_ID, INVALID_SIZE));
    }

    @Test
    public void deleteStockWithNonPresentStockThrowsException() {
        expect(stockService.findByProductIdAndSize(VALID_PRODUCT_ID, VALID_STOCK_SIZE))
            .andReturn(Optional.empty()).atLeastOnce();
        replay();

        assertThrows(ResponseStatusException.class, () ->
            stockController.deleteStock(VALID_PRODUCT_ID, VALID_STOCK_SIZE));
    }

    @Test
    public void deleteStockWithPresentStockReturnsOk() {
        Stock deletableStock = getValidStock();

        expect(stockService.findByProductIdAndSize(VALID_PRODUCT_ID, VALID_STOCK_SIZE))
            .andReturn(Optional.of(deletableStock)).atLeastOnce();
        stockService.deleteStock(deletableStock);
        expectLastCall().atLeastOnce();
        replay();

        ResponseEntity<Stock> response = stockController.deleteStock(VALID_PRODUCT_ID, VALID_STOCK_SIZE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void DeleteAllStocksByProductId_WithInvalidId_ThrowsException() {
        assertThrows(ResponseStatusException.class, () ->
            stockController.deleteAllStocksByProductId(NEGATIVE_ID));

        assertThrows(ResponseStatusException.class, () ->
            stockController.deleteAllStocksByProductId(ZERO_ID));
    }

    @Test
    public void DeleteAllStocksByProductId_WithNoProductPresent_ThrowsException() {
        expect(stockService.findByProductId(VALID_PRODUCT_ID)).andReturn(new ArrayList<>()).atLeastOnce();
        replay();

        assertThrows(ResponseStatusException.class, () ->
            stockController.deleteAllStocksByProductId(VALID_PRODUCT_ID));
    }

    @Test
    public void DeleteAllStocksByProductId_WithProductIsPresent_ReturnsOk() {
        List<Stock> nonEmptyList = getStockList(VALID_PRODUCT_ID, VALID_STOCK_SIZE);
        expect(stockService.findByProductId(VALID_PRODUCT_ID)).andReturn(nonEmptyList).atLeastOnce();
        stockService.deleteAllStocks(nonEmptyList);
        expectLastCall();
        replay();

        ResponseEntity<List<Stock>> response = stockController.deleteAllStocksByProductId(VALID_PRODUCT_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<Stock> getStockList(Long productId, String size) {
        List<Stock> stockList = new ArrayList<>(STOCK_LIST_SIZE);

        for (int i = 0; i < STOCK_LIST_SIZE; i++) {
            Product newProduct = new Product();
            newProduct.setId(productId);

            Stock newStock = new Stock((long) i, newProduct, size, VALID_STOCK_QUANTITY);

            stockList.add(newStock);
        }

        return stockList;
    }

    private StockDto getValidStockDto() {
        StockDto stockDto = new StockDto();
        stockDto.setProductId(VALID_PRODUCT_ID);
        stockDto.setSize(VALID_STOCK_SIZE);
        stockDto.setQuantity(VALID_STOCK_QUANTITY);

        return stockDto;
    }

    private Stock getValidStock() {
        Product product = new Product();
        product.setId(VALID_PRODUCT_ID);

        return new Stock(VALID_STOCK_ID, product, VALID_STOCK_SIZE, VALID_STOCK_QUANTITY);
    }

    private Stock mapDtoToStock(StockDto stockDto) {
        Product product = new Product();
        product.setId(stockDto.getProductId());

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setSize(stockDto.getSize());
        stock.setQuantity(stockDto.getQuantity());

        return stock;
    }
}