package org.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.store.entity.ProductEntity;

public record ProductDto(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @Min(1)
        Double price,
        @NotBlank
        String category) {
    public static ProductDto fromEntity(ProductEntity productEntity) {
        return new ProductDto(productEntity.getName(), productEntity.getDescription(),
                productEntity.getPrice(), productEntity.getCategory());
    }
}
