package com.tranxuanphong.peterservice.repository.mongo;


import com.tranxuanphong.peterservice.entity.ProductVoucher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductVoucherRepository extends MongoRepository<ProductVoucher, String> {
    List<ProductVoucher> findByVariantId(String variantId);
}

