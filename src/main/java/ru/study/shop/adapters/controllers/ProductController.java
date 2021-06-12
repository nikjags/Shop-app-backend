package ru.study.shop.adapters.controllers;

import org.apache.commons.validator.GenericValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.study.shop.adapters.controllers.dto.ProductDto;
import ru.study.shop.adapters.controllers.utils.dto_validation.groups.OnCreate;
import ru.study.shop.adapters.controllers.utils.dto_validation.groups.OnUpdate;
import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQueryConstraints;
import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQueryConstraints.Builder;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.ProductService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final String NO_SUCH_PRODUCT_MESSAGE = "no product with such ID is present";
    private static final String INVALID_ID_MESSAGE = "invalid ID; must be more than 0";
    private static final String NO_PROPERTIES_TO_UPDATE_MESSAGE = "no properties to update in request body";

    private static final Class<OnCreate> CREATE_OPTION = OnCreate.class;
    private static final Class<OnUpdate> UPDATE_OPTION = OnUpdate.class;

    private final ProductService productService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Product>> getProductList(
        @RequestParam(name = "fromId") Optional<String> strFromId,
        @RequestParam(name = "toId") Optional<String> strToId,
        @RequestParam(name = "names") Optional<List<String>> productNames,
        @RequestParam(name = "types") Optional<List<String>> productTypes,
        @RequestParam(name = "manufacturers") Optional<List<String>> productManufacturers,
        @RequestParam(name = "materials") Optional<List<String>> productMaterials,
        @RequestParam(name = "fromPrice") Optional<String> strFromPrice,
        @RequestParam(name = "toPrice") Optional<String> strToPrice) {

        Builder builder = ProductQueryConstraints.getConstraintsBuilder();

        Long fromId = null;
        Long toId = null;
        if (strFromId.isPresent() && GenericValidator.isLong(strFromId.get())) {
            fromId = Long.valueOf(strFromId.get());
        }
        if (strToId.isPresent() && GenericValidator.isLong(strToId.get())) {
            toId = Long.valueOf(strToId.get());
        }
        builder.idConstraint(fromId, toId);

        productNames.ifPresent(builder::nameConstraint);

        productTypes.ifPresent(builder::typeConstraint);

        productManufacturers.ifPresent(builder::manufacturerConstraint);

        productMaterials.ifPresent(builder::materialConstraint);

        Long fromPrice = null;
        Long toPrice = null;
        if (strFromPrice.isPresent() && GenericValidator.isLong(strFromPrice.get())) {
            fromPrice = Long.valueOf(strFromPrice.get());
        }
        if (strToPrice.isPresent() && GenericValidator.isLong(strToPrice.get())) {
            toPrice = Long.valueOf(strToPrice.get());
        }
        builder.priceConstraint(fromPrice, toPrice);

        ProductQueryConstraints builtQuery = builder.build();

        return ResponseEntity.ok(productService.findByProductQuery(builtQuery));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        validateProductId(productId);

        return productService.findById(productId)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_PRODUCT_MESSAGE));
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto newProduct) {
        validateProductDto(newProduct, CREATE_OPTION);

        return ResponseEntity.ok(productService.saveProduct(mapDtoToProduct(newProduct)));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable("id") Long productId, @RequestBody ProductDto productChanges) {
        validateProductId(productId);
        validateProductDto(productChanges, UPDATE_OPTION);

        Product editableProduct = checkForProductPresenceAngGet(productId);

        updateProduct(editableProduct, productChanges);
        productService.saveProduct(editableProduct);

        return ResponseEntity.ok(editableProduct);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long productId) {
        validateProductId(productId);

        Product deletableProduct = checkForProductPresenceAngGet(productId);
        productService.deleteProduct(deletableProduct);

        return ResponseEntity.ok().build();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateProductId(Long productId) {
        if (isNull(productId) || productId < 1) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, INVALID_ID_MESSAGE
            );
        }
    }

    private void validateProductDto(ProductDto newProduct, Class<?> option) {
        if (isNull(newProduct)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                NO_PROPERTIES_TO_UPDATE_MESSAGE);
        }

        if (isNull(option) || !option.equals(UPDATE_OPTION)) {
            option = CREATE_OPTION;
        }

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(newProduct, option);

        if (!violations.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (ConstraintViolation<ProductDto> violation : violations) {
                str.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, str.toString());
        }
    }

    private Product checkForProductPresenceAngGet(Long productId) {
        return productService
            .findById(productId)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_PRODUCT_MESSAGE));
    }

    private void updateProduct(Product editableProduct, ProductDto productChanges) {
        if (nonNull(productChanges.getProductName())) {
            editableProduct.setProductName(productChanges.getProductName());
        }
        if (nonNull(productChanges.getProductType())) {
            editableProduct.setProductType(productChanges.getProductType());
        }
        if (nonNull(productChanges.getMaterial())) {
            editableProduct.setMaterial(productChanges.getMaterial());
        }
        if (nonNull(productChanges.getManufacturer())) {
            editableProduct.setManufacturer(productChanges.getManufacturer());
        }
        if (nonNull(productChanges.getDescription())) {
            editableProduct.setDescription(productChanges.getDescription());
        }
        if (nonNull(productChanges.getPrice())) {
            editableProduct.setPrice(productChanges.getPrice());
        }
    }

    private Product mapDtoToProduct(ProductDto productDto) {
        return new Product(
            productDto.getProductName(),
            productDto.getProductType(),
            productDto.getMaterial(),
            productDto.getManufacturer(),
            productDto.getDescription(),
            productDto.getPrice()
        );
    }
}
