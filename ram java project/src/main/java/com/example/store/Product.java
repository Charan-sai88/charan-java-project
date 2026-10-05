package com.example.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Enter a product name.")
    @Size(max = 120, message = "Product name must be 120 characters or fewer.")
    @Column(nullable = false, length = 120)
    private String name;

    @Size(max = 500, message = "Description must be 500 characters or fewer.")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Enter a price.")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits before the decimal and 2 after it.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    protected Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setDescription(String description) {
        this.description = description == null || description.isBlank() ? null : description.trim();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
