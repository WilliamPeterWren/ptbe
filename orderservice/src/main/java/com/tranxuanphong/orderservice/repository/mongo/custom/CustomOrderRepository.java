package com.tranxuanphong.orderservice.repository.mongo.custom;

import com.tranxuanphong.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomOrderRepository {
    Page<Order> findOrdersByUserIdAndStatusSortedByStatusCreatedAt(String userId, String status, Pageable pageable);
}
