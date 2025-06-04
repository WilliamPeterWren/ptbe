package com.tranxuanphong.peterservice.service;

import java.time.Instant;
import java.util.ArrayList;
// import java.util.Collections;
// import java.util.HashSet;
import java.util.List;
import java.util.Random;
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
import com.tranxuanphong.peterservice.dto.response.FlashSaleItemsResponse;
import com.tranxuanphong.peterservice.dto.response.FlashSaleProductResponse;
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
    if(!productClient.doesProductExistBySellerId(request.getProductId(), sellerId)){
      throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
    }
    FlashSale flashSale = flashSaleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    System.out.println("yes 00");
    if(flashSale.getExpiredAt().isBefore(Instant.now())){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    FlashSaleItem item = FlashSaleItem.builder()
    .productId(request.getProductId())
    .price(request.getPrice())
    .build();
    
    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();
    items.add(item);

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

  public List<FlashSaleItemsResponse> flashSaleItemsResponse(String flashSaleId){
    System.out.println("yes 00");
    FlashSale flashSale = flashSaleRepository.findById(flashSaleId).orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    
    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();

    System.out.println("size: "+items.size());

    List<String> ids = new ArrayList<>();
    
    List<FlashSaleItem> listRand = new ArrayList<>(items);
    
    for(int i = 0; i < 10; i++){
      
      Random random = new Random();
      FlashSaleItem randomItem = listRand.get(random.nextInt(listRand.size()));

      if(!ids.contains(randomItem.getProductId())) {
        ids.add(randomItem.getProductId());
      }
      else{
        i--;
      }

    }

   
    System.out.println("yes 02");
    
    List<FlashSaleProductResponse> list = productClient.getListProductByIds(ids);
    System.out.println("yes 03");


    List<FlashSaleItemsResponse> listFlashSaleItemsResponses = new ArrayList<>();
    System.out.println("yes 04");


    for(FlashSaleProductResponse p : list){
      System.out.println("yes 05");

      FlashSaleItem flashSaleItem = getFlashSaleItemByProductId(p.getId());
      System.out.println("yes 06");

      FlashSaleItemsResponse flashSaleItemsResponse = FlashSaleItemsResponse.builder()
      .id(p.getId())
      .images(p.getImages())
      .productName(p.getProductName())
      .slug(p.getSlug())
      .price(p.getPrice())
      .salePrice(p.getSalePrice())
      .discount(flashSaleItem.getPrice())
      .username(p.getUsername())
      .stock(p.getStock())
      .build();
      listFlashSaleItemsResponses.add(flashSaleItemsResponse);
    }
    System.out.println("yes 07");

    return listFlashSaleItemsResponses;
  }

  public FlashSaleItem getFlashSaleItemByProductId(String productId) {
    return flashSaleRepository.findFlashSaleItemByProductId(productId);
  }

  public List<FlashSaleItemsResponse> flashSaleItemsResponsePage(String flashSaleId){
    System.out.println("yes 00");
    FlashSale flashSale = flashSaleRepository.findById(flashSaleId).orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    
    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();

    System.out.println("size: "+items.size());

    List<String> productIds = new ArrayList<>();
    
    List<FlashSaleItem> listRand = new ArrayList<>(items);

    // List<FlashSaleItem> sliced = listRand.subList(0, 48);

    // Collections.shuffle(sliced);

    // for(FlashSaleItem item : items){
    //   ids.add(item.getProductId());
    // }
    
    for(int i = 0;  i < listRand.size() && i < 48; i++){
      
      Random random = new Random();
      FlashSaleItem randomItem = listRand.get(random.nextInt(listRand.size()));

      if(!productIds.contains(randomItem.getProductId())) {
        productIds.add(randomItem.getProductId());
      }
      else{
        i--;
      }

    }
  
    System.out.println("yes 02");
    
    List<FlashSaleProductResponse> list = productClient.getListProductByIds(productIds);
    System.out.println("yes 03");


    List<FlashSaleItemsResponse> listFlashSaleItemsResponses = new ArrayList<>();
    System.out.println("yes 04");


    for(FlashSaleProductResponse p : list){
      System.out.println("yes 05");

      FlashSaleItem flashSaleItem = getFlashSaleItemByProductId(p.getId());
      System.out.println("yes 06");

      FlashSaleItemsResponse flashSaleItemsResponse = FlashSaleItemsResponse.builder()
      .id(p.getId())
      .images(p.getImages())
      .productName(p.getProductName())
      .slug(p.getSlug())
      .price(p.getPrice())
      .salePrice(p.getSalePrice())
      .discount(flashSaleItem.getPrice())
      .username(p.getUsername())
      .stock(p.getStock())
      .build();
      listFlashSaleItemsResponses.add(flashSaleItemsResponse);
    }
    System.out.println("yes 07");

    return listFlashSaleItemsResponses;
  }

}
