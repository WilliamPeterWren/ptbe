package com.tranxuanphong.productservice.repository.mongo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.productservice.entity.Product;
import org.springframework.data.domain.Pageable;
import java.util.List;


@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
  boolean existsByProductName(String name);
  Optional<Product> findBySlug(String slug);
  boolean existsById(String id);
  Page<Product> findBySellerId(String sellerId, Pageable pageable);

  List<Product> findBySellerId(String sellerId);

  @Query("{ 'variants.id': ?0 }")
  boolean existsByVariantId(String variantId);

  boolean existsByCategoryId(String id);
}

