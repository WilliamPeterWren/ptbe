package com.tranxuanphong.peterservice.repository.mongo;


import com.tranxuanphong.peterservice.entity.Shipping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends MongoRepository<Shipping, String> {
    Shipping findBySlug(String slug);
}

