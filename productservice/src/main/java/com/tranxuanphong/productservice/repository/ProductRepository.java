package com.tranxuanphong.productservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.productservice.entity.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
  boolean existsByProductName(String name);
  Optional<Product> findBySlug(String slug);
  boolean existsById(String id);
}

