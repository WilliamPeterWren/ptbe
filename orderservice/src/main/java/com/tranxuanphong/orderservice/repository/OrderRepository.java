package com.tranxuanphong.orderservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.orderservice.entity.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
  
}
