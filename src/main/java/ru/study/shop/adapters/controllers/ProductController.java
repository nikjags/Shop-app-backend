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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {

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
            .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto newProduct) {
        validateProductDto(newProduct);

        return ResponseEntity.ok(productService.saveProduct(mapDtoToProduct(newProduct)));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable("id") Long productId, @RequestBody ProductDto productChanges) {
        validateId(productId);

        Optional<Product> productOptional = productService.findById(productId);
        if (!productOptional.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "no product with id = " + productId + " is present");
        }

        Product editableProduct = productOptional.get();
        updateProduct(editableProduct, productChanges);
        return ResponseEntity.ok(productService.saveProduct(editableProduct));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long productId) {
        validateId(productId);

        productService.deleteById(productId);

        return ResponseEntity.accepted().build();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateId(Long productId) {
        if (productId < 1) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "id path variable must be more than 0");
        }
    }

    private void updateProduct(Product editableProduct, ProductDto productChanges) {
        if (Objects.nonNull(productChanges.getProductName())) {
            editableProduct.setProductName(productChanges.getProductName());
        }
        if (Objects.nonNull(productChanges.getProductType())) {
            editableProduct.setProductType(productChanges.getProductType());
        }
        if (Objects.nonNull(productChanges.getMaterial())) {
            editableProduct.setMaterial(productChanges.getMaterial());
        }
        if (Objects.nonNull(productChanges.getManufacturer())) {
            editableProduct.setManufacturer(productChanges.getManufacturer());
        }
        if (Objects.nonNull(productChanges.getDescription())) {
            editableProduct.setDescription(productChanges.getDescription());
        }
        if (Objects.nonNull(productChanges.getPrice())) {
            editableProduct.setPrice(productChanges.getPrice());
        }
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
