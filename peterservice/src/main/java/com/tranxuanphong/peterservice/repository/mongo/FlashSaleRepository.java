package com.tranxuanphong.peterservice.repository.mongo;


import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.repository.custom.FlashSaleRepositoryCustom;

@Repository
public interface FlashSaleRepository extends MongoRepository<FlashSale, String>, FlashSaleRepositoryCustom  {
  boolean existsByName(String name);

  List<FlashSale> findByExpiredAtAfter(Instant now);
}
