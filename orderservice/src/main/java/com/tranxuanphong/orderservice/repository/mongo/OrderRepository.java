package com.tranxuanphong.orderservice.repository.mongo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable;
import com.tranxuanphong.orderservice.entity.Order;
import com.tranxuanphong.orderservice.enums.OrderStatus;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
  boolean existsByAddressId(String id);

  List<Order> findByUserId(String userId);

  Page<Order> findByUserId(String userId, Pageable pageable);

  Page<Order> findBySellerId(String sellerId, Pageable pageable);

  Page<Order> findByUserIdAndSellerId(String userId, String sellerId, Pageable pageable);

  Page<Order> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

  Page<Order> findBySellerIdOrderByCreatedAtDesc(String sellerId, Pageable pageable);

  Page<Order> findByUserIdAndSellerIdOrderByCreatedAtDesc(String userId, String sellerId, Pageable pageable);

  Page<Order> findByOrderStatus_Status(OrderStatus status, Pageable pageable);

  Page<Order> findBySellerIdAndOrderStatus_StatusAndCreatedAtBetween(
    String sellerId,
    OrderStatus status,
    Instant startOfMonth,
    Instant endOfMonth,
    Pageable pageable
  );

  // Page<Order> findByUserIdAndOrderStatus_Status(
  //   String userId,
  //   OrderStatus status,
  //   Pageable pageable
  // );

  Page<Order> findByUserIdAndOrderStatus_StatusOrderByCreatedAtDesc(
    String userId,
    OrderStatus status,
    Pageable pageable
  );



}
