package com.tranxuanphong.peterservice.repository.mongo;


import com.tranxuanphong.peterservice.entity.ShippingVoucher;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShippingVoucherRepository extends MongoRepository<ShippingVoucher, String> {
  List<ShippingVoucher> findByAvailableIsTrueAndExpiredAtAfter(Instant now);
}
