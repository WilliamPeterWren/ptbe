package com.tranxuanphong.productservice.repository.mongo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.repository.mongo.custom.CustomProductRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;


@Repository
public interface ProductRepository extends MongoRepository<Product, String>, CustomProductRepository  {
  boolean existsByProductName(String name);
  Optional<Product> findBySlug(String slug);
  boolean existsById(String id);
  Page<Product> findBySellerId(String sellerId, Pageable pageable);
  Page<Product> findBySellerIdOrderByCreatedAtDesc(String sellerId, Pageable pageable);
  Page<Product> findBySellerIdAndIsActiveOrderByCreatedAtDesc(String sellerId, boolean isActive, Pageable pageable);
  List<Product> findBySellerId(String sellerId);

  @Query("{ 'variants.id': ?0 }")
  boolean existsByVariantId(String variantId);

  boolean existsByCategoryId(String id);

  List<Product> findByIdIn(List<String> ids);
  Page<Product> findByIdIn(List<String> ids, Pageable pageable);
  List<String> getProductImagesById(String id);

  Page<Product> findByPeterCategory(String peterCategory, Pageable pageable);

  Optional<Product> findByVariants_Id(String variantId);
  // Page<Product> findByProductName(String productName, Pageable pageable);
  @Query("{ $text: { $search: ?0 } }")
  Page<Product> findByProductName(String productName, Pageable pageable);

  @Query("{ $text: { $search: ?0 } }")
  Page<Product> findByProductNameAndSellerId(String productName, String sellerId, Pageable pageable);

  Page<Product> findBySellerIdAndCategoryId(String sellerId, String categoryId, Pageable pageable);
}

