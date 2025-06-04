package com.tranxuanphong.productservice.service;


import com.tranxuanphong.productservice.configuration.SecurityConfig;
import com.tranxuanphong.productservice.dto.response.ReviewResponse;
import com.tranxuanphong.productservice.entity.Review;
import com.tranxuanphong.productservice.repository.httpclient.UserClient;
import com.tranxuanphong.productservice.repository.mongo.ReviewRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {

    ReviewRepository reviewRepository;
    UserClient userClient;
    ModelMapper modelMapper;

    @PreAuthorize("hasRole('ROLE_USER')")
    public Review createReview(Review review) {
        
        return reviewRepository.save(review);
    }


    public Page<ReviewResponse> getReviewByProductId(String productId, int page, int size){
        Page<Review> reviewPage = reviewRepository.findByProductIdOrderByUpdatedAtDesc(productId, PageRequest.of(page,size));

        
        List<ReviewResponse> reviewResponseList = new ArrayList<>();

        for (Review review : reviewPage.getContent()) {
            ReviewResponse reviewResponse = modelMapper.map(review, ReviewResponse.class);
            
            String username = userClient.usernameByUserId(review.getUserId());
            
            reviewResponse.setUsername(username);

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


}

