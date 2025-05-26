package com.tranxuanphong.productservice.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.tranxuanphong.productservice.entity.Category;
import java.util.List;


@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
  boolean existsByCategoryName(String name);
  boolean existsById(String id);
  Optional<Category> findBySlug(String slug);
  List<Category> findBySellerId(String sellerId);
  
}
