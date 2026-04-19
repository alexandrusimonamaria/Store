package org.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.store.entity.ProductEntity;

public record ProductDto(
        Long id,
        @NotBlank(message = "Name of the product cannot be blank!")
        String name,
        @NotBlank
        String description,
        @Min(1)
        Double price,
        @NotBlank
        String category) {
    public static ProductDto fromEntity(ProductEntity productEntity) {
        return new ProductDto(productEntity.getId(), productEntity.getName(), productEntity.getDescription(),
                productEntity.getPrice(), productEntity.getCategory());
    }
}
