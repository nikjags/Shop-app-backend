package ru.study.shop.adapters.controllers;

import org.apache.commons.validator.GenericValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQueryConstraints;
import ru.study.shop.adapters.hibernate.impl.query_classes.ProductQueryConstraints.Builder;
import ru.study.shop.entities.Product;
import ru.study.shop.services.interfaces.ProductService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

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

}
