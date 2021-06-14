package ru.study.shop.adapters.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.study.shop.adapters.controllers.dto.StockDto;
import ru.study.shop.entities.Product;
import ru.study.shop.entities.Stock;
import ru.study.shop.services.interfaces.ProductService;
import ru.study.shop.services.interfaces.StockService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("stocks")
public class StockController {
    private static final String INVALID_ID_MESSAGE = "productId must be positive";
    private static final String NULL_ID_AND_SIZE_MESSAGE = "productId and size must not be null";
    private static final String NO_SUCH_STOCK_MESSAGE = "no such stock with provided productId and size";
    private static final String NO_SUCH_PRODUCT_MESSAGE = "no such product with provided id: ";
    private static final long PHANTOM_VALID_STOCK_QUANTITY = 1L;

    private final StockService stockService;
    private final ProductService productService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public StockController(StockService stockService, ProductService productService) {
        this.stockService = stockService;
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<Stock>> findAll(
        @RequestParam(value = "productId", required = false) Long productId,
        @RequestParam(value = "size", required = false) String size) {

        validateId(productId);

        List<Stock> result;

        if (isNull(size) && isNull(productId)) {
            result = stockService.findAll();
        } else if (nonNull(productId) && isNull(size)) {
            result = stockService.findByProductId(productId);
        } else if (isNull(productId)) {
            result = stockService.findBySize(size);
        } else {
            result = new ArrayList<>();

            stockService
                .findByProductIdAndSize(productId, size)
                .ifPresent(result::add);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<Stock> findByStockId(
        @PathVariable("stockId") Long stockId) {
        validateId(stockId);

        Stock stock = checkForPresenceAndGetStock(stockId);

        return ResponseEntity.ok(stock);
    }

    @GetMapping("/{productId}/{size}")
    public ResponseEntity<Stock> findByProductIdAndSize(
        @PathVariable("productId") Long productId,
        @PathVariable("size") String size) {
        checkForNull(productId, size);
        validateId(productId);

        return ResponseEntity.ok(checkForPresenceAndGetStock(productId, size));
    }

    @PostMapping("/new")
    public ResponseEntity<Stock> createStock(@RequestBody StockDto stockDto) {
        validateStockDto(stockDto);

        Stock newStock = mapDtoToStock(stockDto);

        return ResponseEntity.ok(stockService.saveStock(newStock));
    }

    @PutMapping("/{productId}/{size}")
    public ResponseEntity<Stock> editStock(
        @PathVariable("productId") Long productId,
        @PathVariable("size") String size,
        @RequestBody Long quantity) {
        StockDto dtoForValidation = new StockDto(productId, size, quantity);
        validateStockDto(dtoForValidation);

        Stock editableStock = checkForPresenceAndGetStock(productId, size);
        editableStock.setQuantity(quantity);

        return ResponseEntity.ok(stockService.saveStock(editableStock));
    }

    @DeleteMapping("/{productId}/{size}")
    public ResponseEntity<Stock> deleteStock(
        @PathVariable("productId") Long productId,
        @PathVariable("size") String size) {
        StockDto dtoForValidation = new StockDto(productId, size, PHANTOM_VALID_STOCK_QUANTITY);
        validateStockDto(dtoForValidation);

        Stock deletableStock = checkForPresenceAndGetStock(productId, size);
        stockService.deleteStock(deletableStock);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<List<Stock>> deleteAllStocksByProductId(
        @PathVariable("productId") Long productId) {
        validateId(productId);

        List<Stock> stocksForProduct = stockService.findByProductId(productId);

        if (stocksForProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_PRODUCT_MESSAGE);
        }

        stockService.deleteAllStocks(stocksForProduct);

        return ResponseEntity.ok().build();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Stock checkForPresenceAndGetStock(Long stockId) {
        return stockService.findByStockId(stockId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    NO_SUCH_STOCK_MESSAGE));
    }

    private Stock checkForPresenceAndGetStock(Long productId, String size) {
        return stockService.findByProductIdAndSize(productId, size)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    NO_SUCH_STOCK_MESSAGE));
    }

    private void validateId(Long productId) {
        if (nonNull(productId) && productId < 1) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                INVALID_ID_MESSAGE);
        }
    }

    private void checkForNull(Long productId, String size) {
        if (isNull(productId) || isNull(size)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                NULL_ID_AND_SIZE_MESSAGE);
        }
    }

    private void validateStockDto(StockDto stockDto) {
        Set<ConstraintViolation<StockDto>> violations = validator.validate(stockDto);

        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("violations: [");
            for (ConstraintViolation<StockDto> violation : violations) {
                stringBuilder
                    .append(violation.getPropertyPath())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("; ");
            }
            stringBuilder.append("]");

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, stringBuilder.toString());
        }
    }

    private Stock mapDtoToStock(StockDto stockDto) {
        Optional<Product> optionalProduct = productService.findById(stockDto.getProductId());
        if (!optionalProduct.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                NO_SUCH_PRODUCT_MESSAGE + stockDto.getProductId());
        }

        Stock stock = new Stock();
        stock.setProduct(optionalProduct.get());
        stock.setSize(stockDto.getSize());
        stock.setQuantity(stockDto.getQuantity());

        return stock;
    }
}
