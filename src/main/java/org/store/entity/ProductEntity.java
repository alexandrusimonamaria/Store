package org.store.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private Double price;

    private String category;

}
