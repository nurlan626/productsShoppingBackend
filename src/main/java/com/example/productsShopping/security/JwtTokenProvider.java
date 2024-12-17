package com.example.productsShopping.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // Делает класс доступным для использования в качестве Spring-компонента.
@RequiredArgsConstructor // Генерирует конструктор для всех финальных полей.
public class JwtTokenProvider {

    @Value("${jwt.secret}") // Секретный ключ из application.properties.
    private String jwtSecret;

    @Value("${jwt.expiration}") // Время жизни токена из application.properties.
    private int jwtExpirationInMs;

    // Получение ключа для подписания JWT.
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Генерация JWT токена.
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // Получение данных о пользователе.
        Date now = new Date(); // Текущая дата.
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs); // Дата истечения токена.

        // Создание и возврат токена.
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Установка имени пользователя.
                .setIssuedAt(now) // Установка даты создания токена.
                .setExpiration(expiryDate) // Установка даты истечения токена.
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Подпись токена.
                .compact();
    }

    // Извлечение имени пользователя из токена.
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Установка ключа для проверки подписи.
                .build()
                .parseClaimsJws(token) // Разбор токена.
                .getBody(); // Извлечение данных из токена.
        return claims.getSubject(); // Возврат имени пользователя.
    }

    // Проверка валидности токена.
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Установка ключа для проверки подписи.
                    .build()
                    .parseClaimsJws(authToken); // Проверка токена.
            return true; // Токен валиден.
        } catch (SecurityException ex) {
            // Неверная подпись токена.
        } catch (MalformedJwtException ex) {
            // Неверный формат токена.
        } catch (ExpiredJwtException ex) {
            // Токен истек.
        } catch (UnsupportedJwtException ex) {
            // Токен не поддерживается.
        } catch (IllegalArgumentException ex) {
            // Пустая строка токена.
        }
        return false; // Токен не валиден.
    }
}
