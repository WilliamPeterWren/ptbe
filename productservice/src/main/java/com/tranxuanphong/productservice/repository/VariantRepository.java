package com.tranxuanphong.productservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.productservice.entity.Variant;

@Repository
public interface VariantRepository extends MongoRepository<Variant, String> {
  boolean existsByVariantName(String name);
  boolean existsById(String id);
  Optional<Variant> findById(String id);
  List<Variant> findByProductId(String productId);
}
