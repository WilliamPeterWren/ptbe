package com.tranxuanphong.peterservice.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

// import org.springframework.security.access.prepost.PostAuthorize;
// import org.springframework.security.access.prepost.PreAuthorize;

// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.peterservice.dto.model.UserDTO;
import com.tranxuanphong.peterservice.dto.request.FlashSaleCreateRequest;
import com.tranxuanphong.peterservice.dto.request.FlashSaleUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.mapper.FlashSaleMapper;
import com.tranxuanphong.peterservice.repository.httpclient.UserClient;
import com.tranxuanphong.peterservice.repository.mongo.FlashSaleRepository;
import com.tranxuanphong.peterservice.repository.httpclient.ProductClient;

import org.modelmapper.ModelMapper;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class FlashSaleService {
  FlashSaleRepository flashSaleRepository;
  FlashSaleMapper flashSaleMapper;
  ModelMapper modelMapper;
  ProductClient productClient;
  UserClient userClient;

  @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
  public FlashSaleResponse create(FlashSaleCreateRequest request){
    if(flashSaleRepository.existsByProductId(request.getProductId())){
      throw new AppException(ErrorCode.FLASHSALE_PRODUCT_EXISTS);
    }

    if(request.getExpiredAt().isBefore(Instant.now())){
      throw new AppException(ErrorCode.EXPIRES_INVALID);
    }

    if(!productClient.existsByProductId(request.getProductId())){
      throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
    }

    FlashSale flashSale = flashSaleMapper.toFlashSale(request);
    return flashSaleMapper.toFlashSaleResponse(flashSaleRepository.save(flashSale));
  }

  public Page<FlashSaleResponse> getPaginatedFlashSales(int page, int size) {    
    Page<FlashSale> list = flashSaleRepository.findAll(PageRequest.of(page, size));
    return list.map(flashSale -> modelMapper.map(flashSale, FlashSaleResponse.class));
  }

  public FlashSaleResponse getOne(String id){
    return flashSaleMapper.toFlashSaleResponse(flashSaleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLASHSALE_NOT_FOUND)));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public FlashSaleResponse update(String id, FlashSaleUpdateRequest request){
    if(request.getExpiredAt().isBefore(Instant.now())){
      throw new AppException(ErrorCode.EXPIRES_INVALID);
    }

    FlashSale flashSale = flashSaleMapper.updateFlashSale(flashSaleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLASHSALE_NOT_FOUND)), request);
    return flashSaleMapper.toFlashSaleResponse(flashSale);
  }

  public String delete(String id){
    flashSaleRepository.deleteById(id);
    return "Success delete flashsale";
  }
}
