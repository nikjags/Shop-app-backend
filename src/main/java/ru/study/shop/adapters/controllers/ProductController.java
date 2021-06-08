package ru.study.shop.adapters.controllers;

import org.apache.commons.validator.GenericValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.study.shop.adapters.controllers.dto.ProductDto;
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

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final String NO_SUCH_PRODUCT_MESSAGE = "no product with such ID is present";
    private static final String INVALID_ID_MESSAGE = "invalid ID; must be more than 0";

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
        return productService.findById(productId)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_SUCH_PRODUCT_MESSAGE));
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto newProduct) {
        validateProductDto(newProduct);

        return ResponseEntity.ok(productService.saveProduct(mapDtoToProduct(newProduct)));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable("id") Long productId, @RequestBody ProductDto productChanges) {
        validateProductId(productId);

        Product editableProduct = checkForProductPresenceAngGet(productId);

        boolean isEdited = updateProduct(editableProduct, productChanges);

        if (isEdited) {
            productService.saveProduct(editableProduct);
        }
        return ResponseEntity.ok(editableProduct);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long productId) {
        validateProductId(productId);

        Product deletableProduct = checkForProductPresenceAngGet(productId);
        productService.deleteProduct(deletableProduct);

        return ResponseEntity.accepted().build();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Product checkForProductPresenceAngGet(Long productId) {
        Optional<Product> productOptional = productService.findById(productId);
        if (!productOptional.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, NO_SUCH_PRODUCT_MESSAGE);
        }
        return productOptional.get();
    }

    private void validateProductId(Long productId) {
        if (productId < 1) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, INVALID_ID_MESSAGE
            );
        }
    }

    private boolean updateProduct(Product editableProduct, ProductDto productChanges) {
        boolean isChanged = false;

        if (nonNull(productChanges.getProductName())
            && validateDtoProperty(productChanges, "productName")) {

            editableProduct.setProductName(productChanges.getProductName());
            isChanged = true;
        }
        if (nonNull(productChanges.getProductType())
            && validateDtoProperty(productChanges, "productType")) {

            editableProduct.setProductType(productChanges.getProductType());
            isChanged = true;
        }
        if (nonNull(productChanges.getMaterial())
            && validateDtoProperty(productChanges, "material")) {

            editableProduct.setMaterial(productChanges.getMaterial());
            isChanged = true;
        }
        if (nonNull(productChanges.getManufacturer())
            && validateDtoProperty(productChanges, "manufacturer")) {

            editableProduct.setManufacturer(productChanges.getManufacturer());
            isChanged = true;
        }
        if (nonNull(productChanges.getDescription())
            && validateDtoProperty(productChanges, "description")) {

            editableProduct.setDescription(productChanges.getDescription());
            isChanged = true;
        }
        if (nonNull(productChanges.getPrice())
            && validateDtoProperty(productChanges, "price")) {

            editableProduct.setPrice(productChanges.getPrice());
            isChanged = true;
        }

        return isChanged;
    }

    private boolean validateDtoProperty(ProductDto productChanges, String propertyName) {
        return validator.validateProperty(productChanges, propertyName).isEmpty();
    }

    private void validateProductDto(ProductDto newProduct) {
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(newProduct);

        if (!violations.isEmpty()) {
            StringBuilder str = new StringBuilder();
            for (ConstraintViolation<ProductDto> violation : violations) {
                str.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, str.toString());
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
