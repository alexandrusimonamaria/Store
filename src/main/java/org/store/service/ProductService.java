package org.store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.store.entity.ProductEntity;
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
        return productRepository.findById(id).orElseThrow();
    }

    public ProductEntity addProduct(ProductEntity productEntity) {
        log.info("Adding new product: {}", productEntity);
        return productRepository.save(productEntity);
    }
}
