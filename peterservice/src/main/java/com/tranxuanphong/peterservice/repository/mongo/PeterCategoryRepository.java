package com.tranxuanphong.peterservice.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.peterservice.entity.PeterCategory;

@Repository
public interface PeterCategoryRepository extends MongoRepository<PeterCategory, String> {
  boolean existsByName(String name);
  boolean existsById(String id);
}
