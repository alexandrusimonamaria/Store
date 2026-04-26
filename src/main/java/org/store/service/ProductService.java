package org.store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.store.entity.ProductEntity;
import org.store.exception.ProductNotFoundException;
import org.store.repository.ProductRepository;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity getProductById(Long id) {
        log.info("Getting product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    public ProductEntity addProduct(ProductEntity entity) {
        log.info("Adding new product: {}", entity);
        return productRepository.save(entity);
    }

    public Page<ProductEntity> getAllProducts(Pageable pageable) {
        log.info("Getting all products from the database.");
        return productRepository.findAll(pageable);
    }

    public ProductEntity updateProduct(Long id, Double newPrice) {
        log.info("Updating product: {}", id);
        ProductEntity product = getProductById(id);
        product.setPrice(newPrice);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }
}
