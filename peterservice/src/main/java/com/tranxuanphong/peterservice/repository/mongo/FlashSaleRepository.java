package com.tranxuanphong.peterservice.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.peterservice.entity.FlashSale;

@Repository
public interface FlashSaleRepository extends MongoRepository<FlashSale, String> {
  boolean existsByProductId(String variantId);
}
