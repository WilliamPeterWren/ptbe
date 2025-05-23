package com.tranxuanphong.cartservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tranxuanphong.cartservice.entity.CartItem;
import java.util.List;


@Repository
public interface CartItemRepository extends MongoRepository<CartItem, String> {
  List<CartItem> findByCartId(String cartId);
}
