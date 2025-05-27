package com.tranxuanphong.peterservice.repository.mongo;


import com.tranxuanphong.peterservice.entity.SellerVoucher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SellerVoucherRepository extends MongoRepository<SellerVoucher, String> {
    List<SellerVoucher> findBySellerId(String sellerId);
}
