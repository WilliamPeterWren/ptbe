package com.tranxuanphong.productservice.service;

import com.tranxuanphong.productservice.dto.request.ProductCreateRequest;
import com.tranxuanphong.productservice.dto.request.ProductUpdateRequest;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.entity.Category;
import com.tranxuanphong.productservice.entity.Info;
import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.entity.Variant;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.ProductMapper;
import com.tranxuanphong.productservice.repository.httpclient.FileClient;
import com.tranxuanphong.productservice.repository.httpclient.PeterClient;
import com.tranxuanphong.productservice.repository.httpclient.UserClient;
import com.tranxuanphong.productservice.repository.mongo.CategoryRepository;
import com.tranxuanphong.productservice.repository.mongo.ProductRepository;
import com.tranxuanphong.productservice.utils.GenerateSlug;
import com.tranxuanphong.productservice.utils.GenerateUUID;


import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;


import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class SellerProductService {
  ProductRepository productRepository;
  CategoryRepository categoryRepository;
  
  ProductMapper productMapper;

  UserClient userClient;
  PeterClient peterClient;
  FileClient fileClient;

  GenerateSlug generateSlug;
  GenerateUUID generateUUID;

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public ProductResponse create(ProductCreateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);

    Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORYID_INVALID));
    if(!category.getSellerId().equals(sellerId)){
      throw new AppException(ErrorCode.CATEGORY_SELLER_NOT_MATCH);
    }

    if(!peterClient.existsInPeterCategoryId(request.getPeterCategory())){
      throw new AppException(ErrorCode.CATEGORYID_INVALID);
    }

    List<Product> products = productRepository.findBySellerId(sellerId);
    for(Product prod: products){
      if(prod.getProductName().equals(request.getProductName())){
        throw new AppException(ErrorCode.PRODUCT_EXISTS);
      }
    }

    String slug = generateSlug.generateSlug(request.getProductName());

    Set<Variant> variants = request.getVariants().stream()
      .map(v -> Variant.builder()
        .id(generateUUID.generateUuid())
        .variantName(v.getVariantName())
        .price(v.getPrice())
        .salePrice(v.getSalePrice())
        .stock(v.getStock())
        .createdAt(LocalDate.now())
        .updatedAt(LocalDate.now())
        .build())
      .collect(Collectors.toSet());

    Set<Info> infos = request.getInfos().stream()
      .map(i -> Info.builder()
        .id(generateUUID.generateUuid())
        .name(i.getName())
        .detail(i.getDetail())
        .build())
      .collect(Collectors.toSet());

    Product product = Product.builder()
    .sellerId(sellerId)
    .productName(request.getProductName())
    .categoryId(request.getCategoryId())
    .peterCategory(request.getPeterCategory())
    .productImages(request.getProductImages())
    .variants(variants)
    .infos(infos)
    .slug(slug)
    .description(request.getDescription())
    .build();
    
    return productMapper.toProductResponse(productRepository.save(product));
  }
  
  @PreAuthorize("hasRole('ROLE_SELLER')")
  public ProductResponse updateById(String id, ProductUpdateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);

    if(request.getCategoryId() != null && !request.getCategoryId().isEmpty() ){
      Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORYID_INVALID));
      if(!category.getSellerId().equals(sellerId)){
        throw new AppException(ErrorCode.CATEGORY_SELLER_NOT_MATCH);
      }
    }

    Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));

    if(request.getProductName() != null && !request.getProductName().equals(product.getProductName()) && !request.getProductName().isEmpty() ){
      List<Product> products = productRepository.findBySellerId(sellerId);
      for(Product prod: products){
        if(prod.getProductName().equals(request.getProductName())){
          throw new AppException(ErrorCode.PRODUCT_EXISTS);
        }
      }

      product.setProductName(request.getProductName());
      String updateSlug = generateSlug.generateSlug(request.getProductName());
      product.setSlug(updateSlug);
    }

    if(request.getCategoryId() != null && !request.getCategoryId().equals(product.getCategoryId()) && !request.getCategoryId().isEmpty() ){
      product.setCategoryId(request.getCategoryId());
    }

    if(request.getProductImages() != null && !request.getProductImages().equals(product.getProductImages()) && !request.getProductImages().isEmpty()){
      
      Set<String> set1 = product.getProductImages();
      Set<String> set2 = request.getProductImages();

      List<String> files = new ArrayList<>(set1);
      files.removeAll(set2);

      try {        
        fileClient.deleteFilesProduct(files);
      } catch (Exception e) {
        System.out.println("error: " + e.getMessage());
      }
      
      product.setProductImages(request.getProductImages());
    }

    if(request.getDescription() != null &&  !request.getDescription().equals(product.getDescription()) && !request.getDescription().isEmpty() ){
      product.setDescription(request.getDescription());
    }

    if (request.getVariants() != null && !request.getVariants().equals(product.getVariants()) && !request.getVariants().isEmpty()) {
      Set<Variant> currentVariants = product.getVariants();
 
      Set<Variant> requestVariants = request.getVariants();
  
      Map<String, Variant> variantNameMap = currentVariants.stream()
        .collect(Collectors.toMap(Variant::getVariantName, v -> v, (a, b) -> a));
  
      for (Variant newVariant : requestVariants) {
        boolean isNewVariant = newVariant.getId() == null || newVariant.getId().isEmpty();
  
        if (isNewVariant) {
          if (variantNameMap.containsKey(newVariant.getVariantName())) {
            throw new AppException(ErrorCode.VARIANT_NAME_INVALID);
          }

          Variant variant = Variant.builder()
            .variantName(newVariant.getVariantName())
            .price(newVariant.getPrice())
            .salePrice(newVariant.getSalePrice())
            .stock(newVariant.getStock())
            .build();
  
          currentVariants.add(variant);
          variantNameMap.put(variant.getVariantName(), variant); 
        } else {
          for(Variant match: currentVariants){
            if(match.getId().equals(newVariant.getId())){
            
              String newName = newVariant.getVariantName();
              if (newName != null && !newName.isEmpty() &&
                !newName.equals(match.getVariantName())) {
    
                if (variantNameMap.containsKey(newName)) {
                  throw new AppException(ErrorCode.VARIANT_NAME_INVALID);
                }
    
                match.setVariantName(newName);
              }

              if (newVariant.getPrice() != null && !newVariant.getPrice().equals(match.getPrice())) {
                match.setPrice(newVariant.getPrice());
              }

              if (newVariant.getSalePrice() != null && !newVariant.getSalePrice().equals(match.getSalePrice())) {
                match.setSalePrice(newVariant.getSalePrice());
              }
    
              if (newVariant.getStock() != null && !newVariant.getStock().equals(match.getStock())) {
                match.setStock(newVariant.getStock());
              }

              match.setUpdatedAt(LocalDate.now());
              break;
            }
          }
        
        }
      }
  
      product.setVariants(currentVariants);
    }

    if (request.getInfos() != null && !request.getInfos().equals(product.getInfos()) && !request.getInfos().isEmpty()) {
      
      Set<Info> currentInfos = product.getInfos();
      Set<Info> requestInfos = request.getInfos();
  
      Map<String, Info> infoNameMap = currentInfos.stream()
        .collect(Collectors.toMap(Info::getName, i -> i, (a, b) -> a));
  
      for (Info newInfo : requestInfos) {
        boolean isNewInfo = newInfo.getId() == null || newInfo.getId().isEmpty();

        if (isNewInfo) {
          boolean nameExists = currentInfos.stream()
            .anyMatch(info -> info.getName().equalsIgnoreCase(newInfo.getName()));
          if (nameExists) {
            throw new AppException(ErrorCode.INFO_NAME_DUPLICATE);
          }

          currentInfos.add(Info.builder()
            .name(newInfo.getName())
            .detail(newInfo.getDetail())
            .build()
          );
        } else {
          for(Info match: currentInfos){
            if(match.getId().equals(newInfo.getId())){
            
              boolean isSame =
              match.getName().equals(newInfo.getName()) &&           
              match.getDetail().equals(newInfo.getDetail());
  
              if (isSame) {
                throw new AppException(ErrorCode.INFO_EXISTS);
              }
  
              String newName = newInfo.getName();
              if (newName != null && !newName.isEmpty() &&
                !newName.equals(match.getName())) {
    
                if (infoNameMap.containsKey(newName)) {
                  throw new AppException(ErrorCode.INFO_NAME_DUPLICATE);
                }
    
                match.setName(newName);
              }   
    
              if (newInfo.getDetail() != null && !newInfo.getDetail().equals(match.getDetail())) {
                match.setDetail(newInfo.getDetail());
              }    
         
              break;
            }
          }        
        }
      }
  
      product.setInfos(currentInfos);
    } 

    product.setUpdatedAt(Instant.now());
  
    return productMapper.toProductResponse(productRepository.save(product));
  }

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public ProductResponse delete(String id){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);

    Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));

    if(!product.getSellerId().equals(sellerId)){
      throw new AppException(ErrorCode.PRODUCT_SELLER_NOT_MATCH);
    }

    Set<Variant> variants = product.getVariants();

    boolean isDelete = true;

    if(isDelete){
      // productRepository.deleteById(id);
      product.setIsActive(false);
    }
    else{
      for(Variant v: variants){
        v.setStock(0L);    
      }
    }

    return productMapper.toProductResponse(productRepository.save(product));
  }
  
  public void saveProductImageMetadata(String productId, List<String> fileNames){
    Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    Set<String> fileNameSet = new HashSet<>(fileNames);
    product.setProductImages(fileNameSet);
    productRepository.save(product);
  }
}
