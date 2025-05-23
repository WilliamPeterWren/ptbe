package com.tranxuanphong.productservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.productservice.entity.Info;
import java.util.List;


@Repository
public interface InfoRepository extends MongoRepository<Info, String> {
  boolean existsByName(String name);
  boolean existsById(String id);
  Optional<Info> findById(String id);
  List<Info> findByProductId(String productId);
} 
