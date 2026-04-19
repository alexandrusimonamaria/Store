package org.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.dto.ProductDto;
import org.store.entity.ProductEntity;
import org.store.exception.ProductNotFoundException;
import org.store.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductEntity product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new ProductEntity();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(4000.0);
        product.setCategory("Electronics");

        productDto = new ProductDto("Laptop", "Gaming laptop", 4000.0, "Electronics");
    }

    @Test
    void getProductByWhenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductEntity result = productService.getProductById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");
    }

    @Test
    void getProductByIdWhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product with id 99 not found");
    }

    @Test
    void addProduct() {
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);

        ProductEntity result = productService.addProduct(productDto);

        assertThat(result.getName()).isEqualTo("Laptop");
        assertThat(result.getPrice()).isEqualTo(4000.0);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductEntity> result = productService.getAllProducts();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Laptop");
    }

    @Test
    void updateProductWhenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);

        ProductEntity result = productService.updateProduct(1L, 5000.0);

        assertThat(result.getPrice()).isEqualTo(5000.0);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteProductWhenExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProductWhenNotFound() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> productService.deleteProduct(99L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product with id 99 not found");
    }
}
