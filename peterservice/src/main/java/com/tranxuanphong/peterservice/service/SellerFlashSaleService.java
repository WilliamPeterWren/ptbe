package com.tranxuanphong.peterservice.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tranxuanphong.peterservice.dto.request.FlashSaleSellerUpdateRequest;
import com.tranxuanphong.peterservice.dto.request.SellerRemoveProductFromFlashsale;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.dto.response.ProductResponse;
import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.entity.FlashSaleItem;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.mapper.FlashSaleMapper;
import com.tranxuanphong.peterservice.repository.httpclient.ProductClient;
import com.tranxuanphong.peterservice.repository.httpclient.UserClient;
import com.tranxuanphong.peterservice.repository.mongo.FlashSaleRepository;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerFlashSaleService {
  FlashSaleRepository flashSaleRepository;
  FlashSaleMapper flashSaleMapper;
  ProductClient productClient;
  UserClient userClient;

  @PreAuthorize("hasAnyRole('ROLE_SELLER')")
  public FlashSaleResponse updateBySeller(String id, FlashSaleSellerUpdateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);
    if(!productClient.doesProductExistBySellerId(request.getProductId(), sellerId)){
      throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
    }
    FlashSale flashSale = flashSaleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    
    if(flashSale.getExpiredAt().isBefore(Instant.now())){
      throw new AppException(ErrorCode.FLASHSALE_EXPIRED);
    }

    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();

    boolean checkExists = false;
    for(FlashSaleItem f : items){
      if(f.getProductId().equals(request.getProductId()) && f.getSellerId().equals(sellerId)){
        f.setPrice(request.getPrice());
        checkExists = true;
        System.out.println("update new discount: " + request.getPrice());
        break;        
      }
    }
    if(!checkExists){
      System.out.println("add product to flash sale: " + request.getProductId());
      FlashSaleItem item = FlashSaleItem.builder()
      .sellerId(sellerId)
      .productId(request.getProductId())
      .price(request.getPrice())
      .build();
      
      items.add(item);
    }
   
    flashSale.setFlashSaleItems(items);

    return flashSaleMapper.toFlashSaleResponse(flashSaleRepository.save(flashSale));
  }

  @PreAuthorize("hasAnyRole('ROLE_SELLER')")
  public Page<ProductResponse> sellerGetProductByFlashsaleId(String id, int page, int size) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    String sellerId = userClient.userId(email);

    FlashSale flashSale = flashSaleRepository.findFlashSaleItemsByIdAndSellerIdSortedByUpdatedAtDesc(id, sellerId)
            .orElseThrow(() -> new RuntimeException("No FlashSaleItems found for flashSaleId: " + id + " and sellerId: " + sellerId));

    List<FlashSaleItem> items = flashSale.getFlashSaleItems().stream()
    .filter(item -> item.getSellerId().equals(sellerId))
    .sorted(Comparator.comparing(FlashSaleItem::getUpdatedAt).reversed())
    .collect(Collectors.toList());

    Pageable pageable = PageRequest.of(page, size);
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), items.size());

    List<ProductResponse> productResponses = items.subList(start, end).stream()
            .map(item -> {
              try {
                // System.out.println("productID: " + item.getProductId());
                ProductResponse productResponse = productClient.getProductResponseByProductId(item.getProductId());

                productResponse.setDiscount(item.getPrice());

                return productResponse;
              } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                return null;
              }
            })
            .filter(response -> response != null) 
            .collect(Collectors.toList());


    return new PageImpl<>(productResponses, pageable, items.size());
  }

  @PreAuthorize("hasAnyRole('ROLE_SELLER')")
  public void sellerRemoveProductFromFlashsale(SellerRemoveProductFromFlashsale request){
    System.out.println("flashsale id: " + request.getFlashsaleId() + " productId: " + request.getProductId());
    FlashSale flashSale = flashSaleRepository.findById(request.getFlashsaleId()).orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));

    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();
    for(FlashSaleItem f : items){
      if(f.getProductId().equals(request.getProductId())){
        items.remove(f);
        break;
      }
    }

    flashSale.setFlashSaleItems(items);

    flashSaleRepository.save(flashSale);
  }

}
