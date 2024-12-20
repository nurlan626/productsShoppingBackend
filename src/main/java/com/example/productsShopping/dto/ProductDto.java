package com.example.productsShopping.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String brand;
    private String model;
    private String category;
    private String description;
    private double price;
    private int rate;
    private String imageUrl;
}