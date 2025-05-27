package com.tranxuanphong.peterservice.repository.mongo;

import com.tranxuanphong.peterservice.entity.PeterVoucher;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PeterVoucherRepository extends MongoRepository<PeterVoucher, String> {
    boolean existsBySlug(String slug);

    List<PeterVoucher> findByExpiredAtAfter(Instant now);
}
