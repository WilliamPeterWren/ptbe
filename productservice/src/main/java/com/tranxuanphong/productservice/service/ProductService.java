package com.tranxuanphong.productservice.service;

import org.springframework.stereotype.Service;


import com.tranxuanphong.productservice.dto.response.CartProductResponse;
import com.tranxuanphong.productservice.dto.response.FlashSaleProductResponse;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.entity.Info;
import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.entity.Variant;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.ProductMapper;
import com.tranxuanphong.productservice.repository.httpclient.PeterClient;
import com.tranxuanphong.productservice.repository.httpclient.UserClient;
import com.tranxuanphong.productservice.repository.mongo.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.modelmapper.ModelMapper;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class ProductService {
  ProductRepository productRepository;
  
  ProductMapper productMapper;
  ModelMapper modelMapper;

  UserClient userClient;
  PeterClient peterClient;
  
  public Page<ProductResponse> getPaginatedProductsBySellerId(String sellerId, int page, int size) {
    System.out.println("service");
    if (!userClient.existsById(sellerId)) {
      throw new AppException(ErrorCode.SELLER_NOT_EXIST);
    }

    System.out.println("seller id: " + sellerId);
    Page<Product> list = productRepository.findBySellerIdOrderByCreatedAtDesc(sellerId, PageRequest.of(page, size));  
   
    List<Product> products = list.getContent();
    System.out.println("--------------------------------------");
    for(Product product : products){
      System.out.println(product.getProductName());
    }
    System.out.println("--------------------------------------");

    System.out.println("size: " + products.size());

    List<ProductResponse> productResponses = new ArrayList<>();
    for (Product product : products) {
      ProductResponse productResponse = productMapper.toProductResponse(product);

      String flashsaleId = null;
      try {
          flashsaleId = peterClient.getLastestFlashSalesId();
      } catch (Exception e) {
          System.out.println("error: " + e.getMessage());
      }

      Long discount = 0L;
      try {
          discount = peterClient.getDiscountByFlashSaleIdAndProductId(flashsaleId, product.getId());
      } catch (Exception e) {
          System.out.println("error: " + e.getMessage());
      }

      System.out.println("active: " + product.getIsActive());

      productResponse.setDiscount(discount);
      productResponses.add(productResponse);
    }
    System.out.println("size 2: " + productResponses.size());
    return new PageImpl<>(productResponses, list.getPageable(), list.getTotalElements());
  }


  public ProductResponse getOneBySlug(String flashsaleId, String slug){
    Product product = productRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    ProductResponse productResponse = new ProductResponse();

    productResponse.setId( product.getId() );
    productResponse.setSellerId( product.getSellerId() );
    productResponse.setProductName( product.getProductName() );
    productResponse.setCategoryId( product.getCategoryId() );
    productResponse.setPeterCategory( product.getPeterCategory() );
    Set<String> set = product.getProductImages();
    if ( set != null ) {
        productResponse.setProductImages( new LinkedHashSet<String>( set ) );
    }
    Set<Variant> set1 = product.getVariants();
    if ( set1 != null ) {
        productResponse.setVariants( new LinkedHashSet<Variant>( set1 ) );
    }
    Set<Info> set2 = product.getInfos();
    if ( set2 != null ) {
        productResponse.setInfos( new LinkedHashSet<Info>( set2 ) );
    }
    productResponse.setSlug( product.getSlug() );
    productResponse.setDescription( product.getDescription() );
    Map<Integer, Long> map = product.getRating();
    if ( map != null ) {
        productResponse.setRating( new LinkedHashMap<Integer, Long>( map ) );
    }
    productResponse.setViews( product.getViews() );    
    productResponse.setIsActive( product.getIsActive() );
    productResponse.setSold( product.getSold() );
    productResponse.setShippingId( product.getShippingId() );
    productResponse.setCreatedAt( product.getCreatedAt() );
    productResponse.setUpdatedAt( product.getUpdatedAt() );

    Long discount = 0L;
    try {
      if(flashsaleId != null){
        discount = peterClient.getDiscountByFlashSaleIdAndProductId(flashsaleId, product.getId());
      }
    } catch (Exception e) {
      System.out.println("erro:" + e.getMessage());
    }
    productResponse.setDiscount(discount);

    return productResponse;
  }

  public ProductResponse getOneById(String id){
    Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    // System.out.println("product name: " + product.getProductName());
    return productMapper.toProductResponse(product);
  }

  public boolean checkProductId(String id){
    return productRepository.existsById(id);
  }

  public boolean checkProductBySellerId(String productId, String sellerId){
    Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    return product.getSellerId().equals(sellerId);
  }

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

  public List<String> getProductImageMetadata(String productId){

    List<String> productImages = productRepository.getProductImagesById(productId);

    return productImages;
  }

  public List<FlashSaleProductResponse> getListProductByIds(List<String> ids){
    List<Product> products = productRepository.findByIdIn(ids);

    List<FlashSaleProductResponse> list = new ArrayList<>();
    for(Product product : products){

      Set<Variant> setVariants = product.getVariants();
      // Long minPrice = Long.MAX_VALUE;
      // Long stock = 0L;
      // for(Variant variant : variants){
      //   if(variant.getPrice()< minPrice){
      //     minPrice = variant.getPrice();
      //   }
      //   stock += variant.getStock();
      // }

      List<Variant> listVariants = new ArrayList<>(setVariants);

      Variant variant = listVariants.get(0);


      String sellerId = product.getSellerId();

      String username = "";
      try {
        username = userClient.usernameByUserId(sellerId);
        
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }

      FlashSaleProductResponse temp = FlashSaleProductResponse.builder()
      .id(product.getId())
      .images(product.getProductImages())
      .productName(product.getProductName())
      .slug(product.getSlug())
      .price(variant.getPrice())
      .salePrice(variant.getSalePrice())
      .username(username)
      .stock(variant.getStock())
      .build();

      list.add(temp);
    }

    return list;
  }

  public List<ProductResponse> getRandomProducts(int limit){
    return productMapper.toListProductResponse(productRepository.getRandomProducts(limit)) ;
  }

  public Page<ProductResponse> getProductByPeterCategory(String peterCategoryId, int page, int size){
    Page<Product> list = productRepository.findByPeterCategory(peterCategoryId, PageRequest.of(page, size));
    return list.map(product -> modelMapper.map(product, ProductResponse.class));
  }

  public ProductResponse findProductByVariantId(String variantId) {
    Product product = productRepository.findByVariants_Id(variantId).orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTS));

    return productMapper.toProductResponse(product);
  }

  public CartProductResponse cartProductResponse(String variantId){
    Product product = productRepository.findByVariants_Id(variantId).orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTS));
    
    String variantName = "";
    Long price = 0L;
    Long saleSprice = 0L;

    Set<Variant> variants = product.getVariants();
    for(Variant variant : variants){
      if(variant.getId().equals(variantId)){
        variantName = variant.getVariantName();
        price = variant.getPrice();
        saleSprice = variant.getSalePrice();
        break;
      }
    }

    Set<String> images = product.getProductImages();
    String image = "";
    for(String i : images){
      image = i;
      break;
    }

    return CartProductResponse.builder()
    .productId(product.getId())
    .productName(product.getProductName())
    .variantId(variantId)
    .variantName(variantName)
    .price(price)
    .salePrice(saleSprice)
    .slug(product.getSlug())
    .image(image)
    .build();
  }

  public Page<ProductResponse> searchByProductName(String productName, int page, int size){
  
    String normalizedProductName = Normalizer.normalize(productName, Normalizer.Form.NFC);
    Page<Product> list = productRepository.findByProductName(normalizedProductName, PageRequest.of(page, size));
    return list.map(product -> modelMapper.map(product, ProductResponse.class));
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public ProductResponse updateRatingById(String id, Integer rating){
  
    Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));

    if (rating < 1 || rating > 5) {
      throw new AppException(ErrorCode.INVALID_RATING); 
    }

    Map<Integer, Long> existingRating = product.getRating();

    existingRating.merge(rating, 1L, Long::sum);
  
    return productMapper.toProductResponse(productRepository.save(product));
  }

  public int countProductBySellerId(String sellerId){
    List<Product> list = productRepository.findBySellerId(sellerId);
    return list.size();
  } 

  public void updateProductViews(String productId){
    Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCTID_INVALID));
    product.setViews(product.getViews() + 1L);
    productRepository.save(product);
  }

  public void updateProductViewsBySLug(String slug){
    Product product = productRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCTID_INVALID));
    product.setViews(product.getViews() + 1L);
    productRepository.save(product);
  }

  public List<ProductResponse> getRandomProductBySellerId(String sellerId, int limit){
    List<Product> list = productRepository.findBySellerId(sellerId);

    List<Product> mutableList = new ArrayList<>(list);

    Collections.shuffle(mutableList);

    List<Product> first10 = mutableList.subList(0, Math.min(limit, list.size()));

    return productMapper.toListProductResponse(first10);
  }

  public Page<ProductResponse> findBySellerIdAndCategoryId(String sellerId, String categoryId, int page, int size){
    Page<Product> listProduct = productRepository.findBySellerIdAndCategoryId(sellerId, categoryId, PageRequest.of(page, size));

    return listProduct.map(product -> modelMapper.map(product, ProductResponse.class));
  }

  public String getSellerIdByProductId(String productId){
    Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCTID_INVALID));
    return product.getSellerId();
  }

}
