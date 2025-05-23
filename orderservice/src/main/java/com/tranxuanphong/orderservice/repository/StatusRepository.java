package com.tranxuanphong.orderservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.orderservice.entity.Status;

@Repository
public interface StatusRepository extends MongoRepository<Status, String> {
  
}
