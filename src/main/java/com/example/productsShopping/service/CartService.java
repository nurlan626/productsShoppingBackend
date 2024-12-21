package com.example.productsShopping.service;


import com.example.productsShopping.dto.CartItemDto;
import com.example.productsShopping.entity.CartItem;
import com.example.productsShopping.entity.Product;
import com.example.productsShopping.entity.User;
import com.example.productsShopping.repository.CartRepository;
import com.example.productsShopping.repository.ProductRepository;
import com.example.productsShopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartItemDto addToCart(String username, Long productId, Integer quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartRepository.findByUser_UsernameAndProduct_Id(username, productId)
                .orElse(new CartItem());

        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItem.getQuantity() == null
                ? quantity
                : cartItem.getQuantity() + quantity);

        CartItem savedCartItem = cartRepository.save(cartItem);
        return mapToDto(savedCartItem);
    }

    @Transactional
    public void removeFromCart(String username, Long productId) {
        CartItem cartItem = cartRepository.findByUser_UsernameAndProduct_Id(username, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartRepository.delete(cartItem);
    }

    @Transactional
    public CartItemDto updateCartItemQuantity(String username, Long productId, Integer quantity) {
        CartItem cartItem = cartRepository.findByUser_UsernameAndProduct_Id(username, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(quantity);
        CartItem updatedCartItem = cartRepository.save(cartItem);
        return mapToDto(updatedCartItem);
    }

    public List<CartItemDto> getUserCart(String username) {
        return cartRepository.findByUser_Username(username).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CartItemDto mapToDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();

        dto.setId(cartItem.getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setBrand(cartItem.getProduct().getBrand());
        dto.setModel(cartItem.getProduct().getModel());
        dto.setCategory(cartItem.getProduct().getCategory());
        dto.setDescription(cartItem.getProduct().getDescription());
        dto.setPrice(cartItem.getProduct().getPrice());
        dto.setRate(cartItem.getProduct().getRate());
        dto.setImageUrl(cartItem.getProduct().getImageUrl());
        dto.setQuantity(cartItem.getQuantity());

        return dto;
    }

    @Transactional
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartRepository.deleteByUser(user);
    }
}