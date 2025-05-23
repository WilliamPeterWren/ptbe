package com.tranxuanphong.productservice.service;
import org.springframework.stereotype.Service;

import com.tranxuanphong.productservice.dto.request.CreateVariantRequest;
import com.tranxuanphong.productservice.dto.request.UpdateVariantRequest;
import com.tranxuanphong.productservice.dto.response.VariantResponse;
import com.tranxuanphong.productservice.entity.Variant;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.VariantMapper;
import com.tranxuanphong.productservice.repository.VariantRepository;
import com.tranxuanphong.productservice.repository.ProductRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class VariantService {
  VariantRepository variantRepository;
  ProductRepository productRepository;
  VariantMapper variantMapper;

  
  public VariantResponse create(CreateVariantRequest request){

    if(!productRepository.existsById(request.getProductId())){
      throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
    }
       
    List<Variant> list = variantRepository.findByProductId(request.getProductId());
    boolean hasName = list.stream().anyMatch(variant -> variant.getVariantName().equals(request.getVariantName()));
    if(hasName){
      throw new AppException(ErrorCode.VARIANT_EXISTS);
    }

    Variant variant = variantMapper.toVariant(request);

    return variantMapper.toVariantResponse(variantRepository.save(variant));
  }

  public List<VariantResponse> getAll(){
    return variantMapper.toListVariantResponse(variantRepository.findAll());
  }

  public List<VariantResponse> getByProductId(String productId){
    return variantMapper.toListVariantResponse(variantRepository.findByProductId(productId));
  }

  public VariantResponse getOne(String id){
    Variant variant = variantRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTS));
    return variantMapper.toVariantResponse(variant);
  }

  public VariantResponse update(String id, UpdateVariantRequest request){
    
    if(!productRepository.existsById(request.getProductId())){
      throw new AppException(ErrorCode.PRODUCT_NOT_EXISTS);
    }

    Variant variant = variantRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTS));
    
    List<Variant> list = variantRepository.findByProductId(request.getProductId());
    boolean checkVariant = false;

    for(Variant v : list){
      if(v.getVariantName().equals(request.getVariantName()) && v.getPrice().equals(request.getPrice()) && v.getStock().equals(request.getStock())){
        checkVariant = true;
        break;
      }
    }

    if(checkVariant){
      throw new AppException(ErrorCode.VARIANT_EXISTS);
    }

    variantMapper.updateVariant(variant, request);

    // variant.setName(request.getName());
    // variant.setDetail(request.getDetail());
    
    variantRepository.save(variant);

    return variantMapper.toVariantResponse(variant);
  }

  public void delete(String id){
    variantRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTS));

    variantRepository.deleteById(id);
  }
}
