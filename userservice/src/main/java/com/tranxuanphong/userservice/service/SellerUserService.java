package com.tranxuanphong.userservice.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.tranxuanphong.userservice.dto.response.SellerInfoResponse;
import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.exception.AppException;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.repository.httpclient.ProductClient;
import com.tranxuanphong.userservice.repository.mongo.UserRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class SellerUserService {
  UserRepository userRepository;
  ProductClient productClient;

  public SellerInfoResponse getSellerInfo(String sellerId){
    User user = userRepository.findById(sellerId).orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
  
    int countProduct = 0;

    try {
      countProduct = productClient.countProductBySellerId(sellerId);
    } catch (Exception e) {
      System.out.println("count product error: " + e.getMessage());
    }

    int follower = user.getFollower().size();
    int following = user.getFollowing().size();

    Map<Integer, Long> rating = user.getRating();

    int countRating = 0;

    double star = 0;
    long totalCount = 0;
    long weightedSum = 0;

    for (Map.Entry<Integer, Long> entry : rating.entrySet()) {
      int key = entry.getKey();     
      long value = entry.getValue(); 

      countRating += (int)value;

      weightedSum += key * value;
      totalCount += value;
    }

    if (totalCount > 0) {
      star = (double) weightedSum / totalCount;
    }
  

    SellerInfoResponse sellerInfoResponse = SellerInfoResponse.builder()
    .sellerId(sellerId)
    .sellerUsername(user.getUsername())
    .createdAt(user.getCreatedAt())
    .countProduct(countProduct)
    .follower(follower)
    .following(following)
    .rating(countRating)
    .star(star)
    .avatar(user.getAvatar())
    .build();

    return sellerInfoResponse;
  }

}
