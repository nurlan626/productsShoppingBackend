package com.example.productsShopping.service;

import com.example.productsShopping.dto.UserProfileDTO;
import com.example.productsShopping.entity.User;
import com.example.productsShopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserProfileDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileDTO(user.getName(), user.getSurname(), user.getEmail(),user.getUsername());
    }
}

