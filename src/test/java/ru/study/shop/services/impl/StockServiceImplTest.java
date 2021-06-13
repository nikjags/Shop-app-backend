package ru.study.shop.services.impl;

import org.assertj.core.util.Lists;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.easymock.annotation.Mock;
import ru.study.shop.adapters.hibernate.StockRepository;
import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;
import ru.study.shop.services.interfaces.StockService;

import java.util.List;

import static org.assertj.core.util.Lists.emptyList;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.unitils.easymock.EasyMockUnitils.replay;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public class StockServiceImplTest {
    public static final String SIZE_REPRESENTED_IN_LIST = "41";
    private static final Product PRODUCT_1 = new Product(
        "Продукт 1",
        "тип 1",
        "материал 1",
        "Производитель 1",
        "",
        1500L);
    private static final Product PRODUCT_2 = new Product(
        "Продукт 2",
        "тип 1",
        "материал 1",
        "Производитель 1",
        "",
        2000L);
    private static final List<Stock> STOCK_LIST = Lists.list(
        new Stock(PRODUCT_1, "40", 50L),
        new Stock(PRODUCT_1, "33", 23L),
        new Stock(PRODUCT_1, "45", 0L),
        new Stock(PRODUCT_2, "41", 32L),
        new Stock(PRODUCT_2, "33", 15L)
    );
    private static final Product PRODUCT_NOT_REPRESENTED_IN_LIST = new Product(
        "Продукт 3",
        "тип 1",
        "материал 1",
        "Производитель 1",
        "",
        2000L);
    @Mock
    StockRepository stockRepository;

    @TestSubject
    StockService stockService;

    @Before
    public void setUp() {
        stockService = new StockServiceImpl(stockRepository);
    }

    @Test
    public void findByProductWithNullProduct() {
        expect(stockRepository.findByProduct(null)).andReturn(emptyList());
        replay();

        List<Stock> result = stockService.findByProduct(null);

        assertEquals(emptyList(), result);
    }

    @Test
    public void findByProductNoStockForProduct() {
        expect(stockRepository.findByProduct(PRODUCT_NOT_REPRESENTED_IN_LIST)).andReturn(emptyList());
        replay();

        List<Stock> result = stockService.findByProduct(PRODUCT_NOT_REPRESENTED_IN_LIST);
        assertEquals(emptyList(), result);
    }

    @Test
    public void findByProductRepresentedInStock() {
        expect(stockRepository.findByProduct(PRODUCT_1)).andReturn(STOCK_LIST);
        replay();

        List<Stock> result = stockService.findByProduct(PRODUCT_1);
        assertEquals(STOCK_LIST, result);
    }


    @Test
    public void findEmptyStocksWithEmptyStocksRepresented() {
        expect(stockRepository.findEmptyStocks()).andReturn(STOCK_LIST);
        replay();

        List<Stock> result = stockService.findEmptyStocks();

        assertEquals(STOCK_LIST, result);
    }

    @Test
    public void findBySizeRepresentedInStock() {
        expect(stockRepository.findBySize(SIZE_REPRESENTED_IN_LIST)).andReturn(STOCK_LIST);
        replay();

        List<Stock> result = stockService.findBySize(SIZE_REPRESENTED_IN_LIST);

        assertEquals(STOCK_LIST, result);
    }
}