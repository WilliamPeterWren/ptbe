package com.tranxuanphong.productservice.repository.mongo;


import com.tranxuanphong.productservice.entity.Review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
  Page<Review> findByProductIdOrderByUpdatedAtDesc(String productId, Pageable pageable);
  
  List<Review> findByProductIdOrderByUpdatedAtDesc(String productId);

  boolean existsByUserIdAndProductIdAndVariantIdAndOrderId(String userId, String productId, String variantId, String orderId);
}