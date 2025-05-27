package com.tranxuanphong.peterservice.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

// import org.springframework.security.access.prepost.PostAuthorize;
// import org.springframework.security.access.prepost.PreAuthorize;

// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.peterservice.dto.request.FlashSaleCreateRequest;
import com.tranxuanphong.peterservice.dto.request.FlashSaleSellerUpdateRequest;
import com.tranxuanphong.peterservice.dto.request.FlashSaleUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.entity.FlashSaleItem;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.mapper.FlashSaleMapper;
import com.tranxuanphong.peterservice.repository.httpclient.UserClient;
import com.tranxuanphong.peterservice.repository.mongo.FlashSaleRepository;
import com.tranxuanphong.peterservice.utils.GenerateSlug;
import com.tranxuanphong.peterservice.repository.httpclient.ProductClient;

import org.modelmapper.ModelMapper;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlashSaleService {
  FlashSaleRepository flashSaleRepository;
  FlashSaleMapper flashSaleMapper;
  ModelMapper modelMapper;
  ProductClient productClient;
  UserClient userClient;
  GenerateSlug generateSlug;

  @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
  public FlashSaleResponse create(FlashSaleCreateRequest request){

    if(request.getExpiredAt().isBefore(Instant.now())){
      throw new AppException(ErrorCode.EXPIRES_INVALID);
    }

    if(flashSaleRepository.existsByName(request.getName())){
      throw new AppException(ErrorCode.FLASHSALE_NAME_EXISTS);
    }
    
    FlashSale flashSale = flashSaleMapper.toFlashSale(request);

    String slug = generateSlug.generateSlug(request.getName());
    flashSale.setSlug(slug);

    return flashSaleMapper.toFlashSaleResponse(flashSaleRepository.save(flashSale));
  }

  public Page<FlashSaleResponse> getPaginatedFlashSales(int page, int size) {    
    Page<FlashSale> list = flashSaleRepository.findAll(PageRequest.of(page, size));
    return list.map(flashSale -> modelMapper.map(flashSale, FlashSaleResponse.class));
  }

  public List<FlashSaleResponse> getValidFlashSales() {
    List<FlashSale> list = flashSaleRepository.findByExpiredAtAfter(Instant.now());
    return flashSaleMapper.toListFlashSaleResponse(list);
  }

  public FlashSaleResponse getOne(String id){
    FlashSale flashSale = flashSaleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    return flashSaleMapper.toFlashSaleResponse(flashSale);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public FlashSaleResponse updateByStaff(String id, FlashSaleUpdateRequest request){
    if(request.getExpiredAt().isBefore(Instant.now())){
      throw new AppException(ErrorCode.EXPIRES_INVALID);
    }

    FlashSale flashSale = flashSaleMapper.updateFlashSale(flashSaleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLASHSALE_NOT_FOUND)), request);
    String slug = generateSlug.generateSlug(request.getName());
    flashSale.setSlug(slug);
    
    return flashSaleMapper.toFlashSaleResponse(flashSale);
  }


  @PreAuthorize("hasAnyRole('ROLE_SELLER')")
  public FlashSaleResponse updateBySeller(String id, FlashSaleSellerUpdateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);

    Set<FlashSaleItem> items = request.getFlashSaleItems();

    for(FlashSaleItem item : items){
      if(!productClient.doesProductExistBySellerId(item.getProductId(), sellerId)){
        throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
      }
    }

    FlashSale flashSale = flashSaleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    
    if(flashSale.getExpiredAt().isBefore(Instant.now())){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    flashSale.setFlashSaleItems(items);
    return flashSaleMapper.toFlashSaleResponse(flashSaleRepository.save(flashSale));
  }


  public String delete(String id){
    flashSaleRepository.deleteById(id);
    return "Success delete flashsale";
  }

  public List<FlashSaleItem> listFlashSaleItems(String flashSaleId, int page, int size) {
    return flashSaleRepository.getFlashSaleItems(flashSaleId, page, size);
  }

  public List<FlashSaleItem> listFlashSaleItems(String flashSaleId) {
    return listFlashSaleItems(flashSaleId, 0, 10);
  }

  
}
