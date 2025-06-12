package com.tranxuanphong.peterservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.stereotype.Service;

import com.tranxuanphong.peterservice.dto.request.FlashSaleCreateRequest;
import com.tranxuanphong.peterservice.dto.response.FlashSaleItemsResponse;
import com.tranxuanphong.peterservice.dto.response.FlashSaleProductResponse;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.entity.FlashSaleItem;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.mapper.FlashSaleMapper;
import com.tranxuanphong.peterservice.repository.mongo.FlashSaleRepository;
import com.tranxuanphong.peterservice.utils.GenerateSlug;
import com.tranxuanphong.peterservice.repository.httpclient.ProductClient;

import org.modelmapper.ModelMapper;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlashSaleService {
  FlashSaleRepository flashSaleRepository;
  FlashSaleMapper flashSaleMapper;
  ProductClient productClient;
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

  // public Page<FlashSaleResponse> getPaginatedFlashSales(int page, int size) {    
  //   Page<FlashSale> list = flashSaleRepository.findAll(PageRequest.of(page, size));
  //   return list.map(flashSale -> modelMapper.map(flashSale, FlashSaleResponse.class));
  // }

  public List<FlashSaleResponse> getValidFlashSales() {
    List<FlashSale> list = flashSaleRepository.findByExpiredAtAfter(Instant.now());
    List<FlashSale> list2 = new ArrayList<>();
    for(FlashSale f: list){
      // System.out.println(f.getAvailable());
      Set<FlashSaleItem> flashSaleItems = f.getFlashSaleItems();
      if(flashSaleItems.size() > 0){
        list2.add(f);
      }
    }
    return flashSaleMapper.toListFlashSaleResponse(list2);
  }

  public FlashSaleResponse getOne(String id){
    FlashSale flashSale = flashSaleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    return flashSaleMapper.toFlashSaleResponse(flashSale);
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
    // System.out.println("yes 00");
    FlashSale flashSale = flashSaleRepository.findById(flashSaleId).orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    
    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();

    // System.out.println("size: "+items.size());

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

   
    // System.out.println("yes 02");
    
    List<FlashSaleProductResponse> list = productClient.getListProductByIds(ids);
    // System.out.println("yes 03");


    List<FlashSaleItemsResponse> listFlashSaleItemsResponses = new ArrayList<>();
    // System.out.println("yes 04");


    for(FlashSaleProductResponse p : list){
      // System.out.println("yes 05");

      FlashSaleItem flashSaleItem = getFlashSaleItemByProductId(p.getId());
      // System.out.println("yes 06");

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
    // System.out.println("yes 07");

    return listFlashSaleItemsResponses;
  }

  public FlashSaleItem getFlashSaleItemByProductId(String productId) {
    return flashSaleRepository.findFlashSaleItemByProductId(productId);
  }

  public List<FlashSaleItemsResponse> flashSaleItemsResponsePage(String flashSaleId){
    FlashSale flashSale = flashSaleRepository.findById(flashSaleId).orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    
    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();


    List<String> productIds = new ArrayList<>();
    
    List<FlashSaleItem> listRand = new ArrayList<>(items);

    for(FlashSaleItem flashSaleItem : listRand){
      productIds.add(flashSaleItem.getProductId());
    }

    List<FlashSaleProductResponse> list = productClient.getListProductByIds(productIds);

    List<FlashSaleItemsResponse> listFlashSaleItemsResponses = new ArrayList<>();

    for(FlashSaleProductResponse p : list){

      FlashSaleItem flashSaleItem = getFlashSaleItemByProductId(p.getId());

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

    Collections.shuffle(listFlashSaleItemsResponses);

    return listFlashSaleItemsResponses;
  }

  public Page<FlashSaleItemsResponse> flashSaleItemsResponsePage(String flashSaleId, int page, int size) {
    FlashSale flashSale = flashSaleRepository.findById(flashSaleId)
        .orElseThrow(() -> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    
    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();
    
    List<FlashSaleItem> listRand = new ArrayList<>(items);
    // Collections.shuffle(listRand); 
    
    Pageable pageable = PageRequest.of(page, size);
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), listRand.size());
    
    List<FlashSaleItem> pagedListRand = listRand.subList(start, end);
    
    List<String> productIds = new ArrayList<>();
    for (FlashSaleItem flashSaleItem : pagedListRand) {
      productIds.add(flashSaleItem.getProductId());
    }
    
    List<FlashSaleProductResponse> list = productClient.getListProductByIds(productIds);
    
    List<FlashSaleItemsResponse> listFlashSaleItemsResponses = new ArrayList<>();
    
    for (FlashSaleProductResponse p : list) {
      FlashSaleItem flashSaleItem = getFlashSaleItemByProductId(p.getId());
      
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
  
    return new PageImpl<>(listFlashSaleItemsResponses, pageable, listRand.size());
  }
  
  public Long getDiscountByFlashSaleIdAndProductId(String flashsaleId, String productId) {
    FlashSale flashSale = flashSaleRepository.findFlashSaleItemsByIdAndProductIdSortedByUpdatedAtDesc(flashsaleId, productId)
            .orElseThrow(() -> new RuntimeException("No FlashSaleItems found for flashSaleId: " + flashsaleId + " and productId: " + productId));

    Set<FlashSaleItem> items = flashSale.getFlashSaleItems();

    if (items.isEmpty()) {
      throw new RuntimeException("No FlashSaleItems found for flashSaleId: " + flashsaleId + " and productId: " + productId);
    }

    return items.iterator().next().getPrice();
  }

  public String getLastestFlashSalesId(){
    List<FlashSale> list = flashSaleRepository.findAll();
    return list.get(0).getId();
  }
}
