package com.example.productsShopping.repository;

import com.example.productsShopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // При необходимости можно добавить кастомные методы
    Optional<Product> findByIdAndUser_Username(Long productId, String username);
}