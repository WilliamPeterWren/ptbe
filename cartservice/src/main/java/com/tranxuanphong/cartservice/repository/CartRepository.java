package com.tranxuanphong.cartservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.cartservice.entity.Cart;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
  Optional<Cart> findByUserId(String userId);
  boolean existsByUserId(String userId);
} 