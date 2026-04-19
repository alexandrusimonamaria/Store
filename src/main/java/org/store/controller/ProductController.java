package org.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import org.store.dto.ProductDto;
import org.store.service.ProductService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("Create new product!");
        ProductDto createdProduct = ProductDto.fromEntity(productService.addProduct(productDto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.id())
                .toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        log.info("Getting product by id!");
        return ResponseEntity.ok(ProductDto.fromEntity(productService.getProductById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("Getting all products!");
        List<ProductDto> products = productService.getAllProducts()
                .stream()
                .map(ProductDto::fromEntity)
                .toList();
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<ProductDto> updateProductPrice(@PathVariable Long id, @RequestParam @Positive(message = "Price must be positive") Double newPrice) {
        log.info("Updating product price!");
        return ResponseEntity.ok(ProductDto.fromEntity(productService.updateProduct(id, newPrice)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product!");
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
