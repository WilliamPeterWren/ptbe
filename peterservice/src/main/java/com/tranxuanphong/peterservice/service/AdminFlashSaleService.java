package com.tranxuanphong.peterservice.service;

import java.time.Instant;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.stereotype.Service;

import com.tranxuanphong.peterservice.dto.request.FlashSaleCreateRequest;
import com.tranxuanphong.peterservice.dto.request.FlashSaleUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.FlashSaleResponse;
import com.tranxuanphong.peterservice.entity.FlashSale;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.mapper.FlashSaleMapper;
import com.tranxuanphong.peterservice.repository.mongo.FlashSaleRepository;
import com.tranxuanphong.peterservice.utils.GenerateSlug;




import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminFlashSaleService {
  FlashSaleRepository flashSaleRepository;
  
  FlashSaleMapper flashSaleMapper;
  ModelMapper modelMapper;

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
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public FlashSaleResponse updateByStaff(String id, FlashSaleUpdateRequest request){
  
    FlashSale flashSale = flashSaleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    
    if(request.getName() != null){
      String slug = generateSlug.generateSlug(request.getName());
      flashSale.setSlug(slug);
      flashSale.setName(request.getName());
    }

    if(request.getExpiredAt() != null && request.getExpiredAt().isAfter(Instant.now())){
      flashSale.setExpiredAt(request.getExpiredAt());
    }

    if(request.getStartedAt() != null && request.getStartedAt().isAfter(Instant.now())){
      flashSale.setStartedAt(request.getStartedAt());
    }

    if(request.getAvailable() != null){
      flashSale.setAvailable(request.getAvailable());
    }
    
    return flashSaleMapper.toFlashSaleResponse(flashSaleRepository.save(flashSale));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public void adminUpdateAll(){
    List<FlashSale> list = flashSaleRepository.findAll();
    for(FlashSale f : list){
      // Set<FlashSaleItem> flashSaleItems = f.getFlashSaleItems();
      // for(FlashSaleItem fl: flashSaleItems){
      //   // String sellerId = "";
      //   // try {
      //   //   sellerId = productClient.getSellerIdByProductId(fl.getProductId());
      //   // } catch (Exception e) {
      //   //   System.out.println("error: " + e.getMessage());
      //   // }
      //   // fl.setSellerId(sellerId);
      //   // fl.setCreatedAt(Instant.now());
      //   // fl.setUpdatedAt(Instant.now());

      //   // if(fl.getSellerId().isEmpty()){
      //   //   flashSaleItems.remove(fl);          
      //   // }     
      // }

      // f.setFlashSaleItems(flashSaleItems);
      f.setAvailable(true);
      flashSaleRepository.save(f);
    }
  }

  public void adminDeleteFlashsale(String id){
    FlashSale flashSale = flashSaleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLASHSALE_NOT_FOUND));
    if(flashSale.getFlashSaleItems().size() == 0){
      flashSaleRepository.deleteById(id);
    }
    else{
      flashSale.setAvailable(false);
      flashSaleRepository.save(flashSale);
    }
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public Page<FlashSaleResponse> getPaginatedFlashSales(int page, int size) {  
    System.out.println("namemmmm");  
    Page<FlashSale> list = flashSaleRepository.findAll(PageRequest.of(page, size));
    // for(FlashSale a : list){
    //   System.out.println(a.getName());
    // }
    return list.map(flashSale -> modelMapper.map(flashSale, FlashSaleResponse.class));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public List<FlashSaleResponse> getListFlashSales() {  
    List<FlashSale> flashSaleResponses = flashSaleRepository.findAll();
    return flashSaleMapper.toListFlashSaleResponse(flashSaleResponses);
  }



}
