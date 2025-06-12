package com.tranxuanphong.productservice.service;


import com.tranxuanphong.productservice.dto.request.ReviewCheckRequest;
import com.tranxuanphong.productservice.dto.response.ReviewResponse;
import com.tranxuanphong.productservice.dto.response.UserResponse;
import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.entity.Review;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.repository.httpclient.UserClient;
import com.tranxuanphong.productservice.repository.mongo.ProductRepository;
import com.tranxuanphong.productservice.repository.mongo.ReviewRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {

    ReviewRepository reviewRepository;
    ProductRepository productRepository;
    UserClient userClient;
    ModelMapper modelMapper;

    @PreAuthorize("hasRole('ROLE_USER')")
    public Review createReview(Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authToken = null;
        if (authentication != null && authentication.getCredentials() instanceof String) {
          authToken = (String) authentication.getCredentials();
        } else if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
          authToken = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal()).getTokenValue();
        }
    
        if (authToken == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String authorizationHeader = "Bearer " + authToken;

        Product product = productRepository.findById(review.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCTID_INVALID));
        Map<Integer, Long> rating = product.getRating();
        int star = review.getStar();
        rating.put(star, rating.getOrDefault(star, 0L) + 1);
        product.setRating(rating);
        productRepository.save(product);

        UserResponse userResponse = userClient.updateRatingBySellerId(star, product.getSellerId(), authorizationHeader);
        System.out.println("rating by: "+userResponse.getEmail());
        return reviewRepository.save(review);
    }


    public Page<ReviewResponse> getReviewByProductId(String productId, int page, int size){
        Page<Review> reviewPage = reviewRepository.findByProductIdOrderByUpdatedAtDesc(productId, PageRequest.of(page,size));

        
        List<ReviewResponse> reviewResponseList = new ArrayList<>();

        for (Review review : reviewPage.getContent()) {
            ReviewResponse reviewResponse = modelMapper.map(review, ReviewResponse.class);
            
            String username = "";
            try {
                username = userClient.usernameByUserId(review.getUserId());                
            } catch (Exception e) {
                System.out.println("Error: "+e.getMessage());
            }

            String avatar = ""; 
            try {                
                avatar = userClient.userAvatar(review.getUserId());
            } catch (Exception e) {
                System.out.println("Error getting user avatar: " + e.getMessage());
            }
            
            reviewResponse.setUsername(username);
            reviewResponse.setAvatar(avatar);

            reviewResponseList.add(reviewResponse);
        } 

        
        
        return new PageImpl<>(reviewResponseList, reviewPage.getPageable(), reviewPage.getTotalElements());
    }
    
    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public Optional<Review> updateReview(String id, Review updatedReview) {


        return reviewRepository.findById(id).map(existingReview -> {
            existingReview.setUserId(updatedReview.getUserId());
            existingReview.setProductId(updatedReview.getProductId());
            existingReview.setVariantId(updatedReview.getVariantId());
            existingReview.setComment(updatedReview.getComment());
            existingReview.setImage(updatedReview.getImage());
            existingReview.setStar(updatedReview.getStar());
            existingReview.setUpdatedAt(Instant.now()); 
            return reviewRepository.save(existingReview);
        });


    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }

    public boolean checkReviewByUserProductVariant(ReviewCheckRequest request){
        return reviewRepository.existsByUserIdAndProductIdAndVariantIdAndOrderId(request.getUserId(), request.getProductId(), request.getVariantId(), request.getOrderId());
    }

}

