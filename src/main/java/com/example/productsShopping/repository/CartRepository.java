package com.example.productsShopping.repository;

import com.example.productsShopping.entity.CartItem;
import com.example.productsShopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser_Username(String username);
    Optional<CartItem> findByUser_UsernameAndProduct_Id(String username, Long productId);

    void deleteByUser(User user); // Изменение метода
}

