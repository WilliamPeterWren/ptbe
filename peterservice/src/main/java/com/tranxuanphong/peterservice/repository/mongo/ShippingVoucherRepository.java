package com.tranxuanphong.peterservice.repository.mongo;


import com.tranxuanphong.peterservice.entity.ShippingVoucher;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShippingVoucherRepository extends MongoRepository<ShippingVoucher, String> {
}
