package com.tranxuanphong.productservice.service;

import org.springframework.stereotype.Service;

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
import com.tranxuanphong.productservice.repository.httpclient.OrderClient;
// import com.tranxuanphong.productservice.repository.elasticsearch.ProductElasticsearchRepository;
import com.tranxuanphong.productservice.repository.httpclient.UserClient;
import com.tranxuanphong.productservice.repository.mongo.CategoryRepository;
import com.tranxuanphong.productservice.repository.mongo.ProductRepository;
import com.tranxuanphong.productservice.utils.GenerateSlug;
import com.tranxuanphong.productservice.utils.GenerateUUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class ProductService {
  ProductRepository productRepository;
  CategoryRepository categoryRepository;
  
  ProductMapper productMapper;
  ModelMapper modelMapper;

  UserClient userClient;
  OrderClient orderClient;

  GenerateSlug generateSlug;
  GenerateUUID generateUUID;

  // ProductElasticsearchRepository productElasticsearchRepository;

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public ProductResponse create(ProductCreateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);

    Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORYID_INVALID));
    if(!category.getSellerId().equals(sellerId)){
      throw new AppException(ErrorCode.CATEGORY_SELLER_NOT_MATCH);
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
    .productImages(request.getProductImages())
    .variants(variants)
    .infos(infos)
    .slug(slug)
    .description(request.getDescription())
    .build();
    
    return productMapper.toProductResponse(productRepository.save(product));
  }
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public Page<ProductResponse> getPaginatedProducts(int page, int size) {    
    Page<Product> list = productRepository.findAll(PageRequest.of(page, size));
    return list.map(product -> modelMapper.map(product, ProductResponse.class));
  }

  public Page<ProductResponse> getPaginatedProductsBySellerId(String sellerId, int page, int size) {    
    if(!userClient.existsById(sellerId)){
      throw new AppException(ErrorCode.SELLER_NOT_EXIST);
    }
    
    Page<Product> list = productRepository.findBySellerId(sellerId, PageRequest.of(page, size));
    return list.map(product -> modelMapper.map(product, ProductResponse.class));
  }

  public ProductResponse getOne(String slug){
    Product product = productRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    return productMapper.toProductResponse(product);
  }

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public ProductResponse updateById(String id, ProductUpdateRequest request){

    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);

    if(request.getCategoryId() != null){
      Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORYID_INVALID));
      if(!category.getSellerId().equals(sellerId)){
        throw new AppException(ErrorCode.CATEGORY_SELLER_NOT_MATCH);
      }
    }

    Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));

    if(request.getProductName() != null){
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

    if(request.getCategoryId() != null){
      product.setCategoryId(request.getCategoryId());
    }

    if(request.getProductImages() != null){
      product.setProductImages(request.getProductImages());
    }

    if(request.getDescription() != null){
      product.setDescription(request.getDescription());
    }

    if (request.getVariants() != null) {
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
            .stock(newVariant.getStock())
            .build();
  
          currentVariants.add(variant);
          variantNameMap.put(variant.getVariantName(), variant); 
        } else {
          Variant match = currentVariants.stream()
            .filter(v -> newVariant.getId().equals(v.getId()))
            .findFirst()
            .orElse(null);

          if (match == null) continue; 

          boolean isSame =
            match.getVariantName().equals(newVariant.getVariantName()) &&
            match.getPrice().equals(newVariant.getPrice()) &&
            match.getStock().equals(newVariant.getStock());

          if (isSame) {
            throw new AppException(ErrorCode.VARIANT_EXISTS);
          }

          String newName = newVariant.getVariantName();
          if (newName != null && !newName.isEmpty() &&
            !newName.equals(match.getVariantName())) {

            if (variantNameMap.containsKey(newName)) {
              throw new AppException(ErrorCode.VARIANT_NAME_INVALID);
            }

            match.setVariantName(newName);
          }

          if (newVariant.getPrice() != null) {
            match.setPrice(newVariant.getPrice());
          }

          if (newVariant.getStock() != null) {
            match.setStock(newVariant.getStock());
          }

          match.setUpdatedAt(LocalDate.now());
        }
      }
  
      product.setVariants(currentVariants);
    }

    if (request.getInfos() != null) {
      Set<Info> currentInfos = product.getInfos();
      Set<Info> requestInfos = request.getInfos();
  
      Map<String, Info> infoIdMap = currentInfos.stream()
        .filter(info -> info.getId() != null && !info.getId().isEmpty())
        .collect(Collectors.toMap(Info::getId, info -> info));
  
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
            Info existingInfo = infoIdMap.get(newInfo.getId());
            if (existingInfo == null) continue; 

            boolean isSame =
              existingInfo.getName().equals(newInfo.getName()) &&
              existingInfo.getDetail().equals(newInfo.getDetail());

            if (isSame) continue; 

            existingInfo.setName(newInfo.getName());
            existingInfo.setDetail(newInfo.getDetail());
          }
      }
  
      product.setInfos(currentInfos);
    } 

    // productElasticsearchRepository.save(updated);
    
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

    for(Variant v: variants){
      if(orderClient.existsByVariantId(v.getId())){
        isDelete = false;
        break;
      }
    }

    if(isDelete){
      productRepository.deleteById(id);
    }
    else{
      for(Variant v: variants){
        v.setStock(0L);    
      }
    }

    return productMapper.toProductResponse(product);
  }


  public boolean checkProductId(String id){
    return productRepository.existsById(id);
  }

  public boolean checkProductBySellerId(String productId, String sellerId){
    Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    return product.getSellerId().equals(sellerId);
  }


  // elasticsearch - not done
    // public List<Product> searchProductsByCriteria(String name, String query, String sellerid, Double minPrice, Double maxPrice) {
  //   return productElasticsearchRepository.searchByCriteria(name, query, sellerid, minPrice, maxPrice);
  // }

  public boolean doesVariantExist(String variantId) {
    return productRepository.existsByVariantId(variantId);
  }

  public boolean doesVariantExistBySellerId(String variantId, String sellerId){
    if(!userClient.existsById(sellerId)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    List<Product> products = productRepository.findBySellerId(sellerId);
    for(Product product : products){
      Set<Variant> variants = product.getVariants();
      for(Variant variant : variants){
        if(variant.getId().equals(variantId)){
          return true;
        }
      }
    }

    return false;
  }

}
