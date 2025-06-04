package com.tranxuanphong.productservice.service;

import org.springframework.stereotype.Service;

import com.tranxuanphong.productservice.dto.request.ProductCreateRequest;
import com.tranxuanphong.productservice.dto.request.ProductUpdateRatingRequest;
import com.tranxuanphong.productservice.dto.request.ProductUpdateRequest;
import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.CartProductResponse;
import com.tranxuanphong.productservice.dto.response.FlashSaleProductResponse;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.entity.Category;
import com.tranxuanphong.productservice.entity.Info;
import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.entity.Variant;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.ProductMapper;
import com.tranxuanphong.productservice.repository.httpclient.OrderClient;
import com.tranxuanphong.productservice.repository.httpclient.PeterClient;
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

import org.modelmapper.ModelMapper;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class ProductService {
  ProductRepository productRepository;
  CategoryRepository categoryRepository;
  
  ProductMapper productMapper;
  ModelMapper modelMapper;

  UserClient userClient;
  OrderClient orderClient;
  PeterClient peterClient;

  GenerateSlug generateSlug;
  GenerateUUID generateUUID;

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public ProductResponse create(ProductCreateRequest request){
    System.out.println("yes 00");
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);
    System.out.println("yes 01");

    Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORYID_INVALID));
    if(!category.getSellerId().equals(sellerId)){
      throw new AppException(ErrorCode.CATEGORY_SELLER_NOT_MATCH);
    }
    System.out.println("yes 02");

    if(!peterClient.existsInPeterCategoryId(request.getPeterCategory())){
      throw new AppException(ErrorCode.CATEGORYID_INVALID);
    }
    System.out.println("yes 03");


    List<Product> products = productRepository.findBySellerId(sellerId);
    for(Product prod: products){
      if(prod.getProductName().equals(request.getProductName())){
        throw new AppException(ErrorCode.PRODUCT_EXISTS);
      }
    }
    System.out.println("yes 04");


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
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public Page<ProductResponse> getPaginatedProducts(int page, int size) {    
    Page<Product> list = productRepository.findAll(PageRequest.of(page, size));
    return list.map(product -> modelMapper.map(product, ProductResponse.class));
  }

  public Page<ProductResponse> getPaginatedProductsBySellerId(String sellerId, int page, int size) {    
    if(!userClient.existsById(sellerId)){
      throw new AppException(ErrorCode.SELLER_NOT_EXIST);
    }
    
    Page<Product> list = productRepository.findBySellerIdOrderByCreatedAtDesc(sellerId, PageRequest.of(page, size));
    return list.map(product -> modelMapper.map(product, ProductResponse.class));
  }

  public ProductResponse getOneBySlug(String slug){
    Product product = productRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    return productMapper.toProductResponse(product);
  }

  public ProductResponse getOneById(String id){
    Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    return productMapper.toProductResponse(product);
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
      System.out.println("update images");
      product.setProductImages(request.getProductImages());
    }
    System.out.println("upddate product images");

    if(request.getDescription() != null &&  !request.getDescription().equals(product.getDescription()) && !request.getDescription().isEmpty() ){
      product.setDescription(request.getDescription());
    }

    if (request.getVariants() != null && !request.getVariants().equals(product.getVariants()) && !request.getVariants().isEmpty()) {
      System.out.println("variant asldkalsdjl");
      Set<Variant> currentVariants = product.getVariants();
 
      Set<Variant> requestVariants = request.getVariants();
  
      Map<String, Variant> variantNameMap = currentVariants.stream()
        .collect(Collectors.toMap(Variant::getVariantName, v -> v, (a, b) -> a));
  
      for (Variant newVariant : requestVariants) {
        boolean isNewVariant = newVariant.getId() == null || newVariant.getId().isEmpty();
  
        if (isNewVariant) {
          System.out.println("new ...");
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
          System.out.println("old ......");
          for(Variant match: currentVariants){
            if(match.getId().equals(newVariant.getId())){
            
              // boolean isSame =
              // match.getVariantName().equals(newVariant.getVariantName()) &&
              // match.getPrice().equals(newVariant.getPrice()) &&
              // match.getSalePrice().equals(newVariant.getSalePrice()) &&
              // match.getStock().equals(newVariant.getStock());

            
              
              System.out.println("yens ....");
  
              // if (isSame) {
              //   throw new AppException(ErrorCode.VARIANT_EXISTS);
              // }

              // if(match.equals(newVariant)){
              //   System.out.println("over ride.....");
              //   throw new AppException(ErrorCode.VARIANT_EXISTS);
              // }

  System.out.println("not same");
              String newName = newVariant.getVariantName();
              if (newName != null && !newName.isEmpty() &&
                !newName.equals(match.getVariantName())) {
    
                if (variantNameMap.containsKey(newName)) {
                  throw new AppException(ErrorCode.VARIANT_NAME_INVALID);
                }
    
                match.setVariantName(newName);
              }

              System.out.println("new name.........");
    
    
              if (newVariant.getPrice() != null && !newVariant.getPrice().equals(match.getPrice())) {
                match.setPrice(newVariant.getPrice());
              }

              System.out.println("price........");

              if (newVariant.getSalePrice() != null && !newVariant.getSalePrice().equals(match.getSalePrice())) {
                match.setSalePrice(newVariant.getSalePrice());
              }

              System.out.println("sale........");
    
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

    System.out.println("upddate product variant");


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

    System.out.println("upddate product info");

    product.setUpdatedAt(Instant.now());

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

    if(isDelete){
      // productRepository.deleteById(id);
      product.setActive(false);
    }
    else{
      for(Variant v: variants){
        v.setStock(0L);    
      }
    }

    return productMapper.toProductResponse(productRepository.save(product));
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteByAdmin(){

    List<Product> products = productRepository.findAll();

    for(Product product : products){
      Set<Variant> variants = product.getVariants();
      boolean check = false;
      for(Variant variant : variants){
        if(variant.getId() == null){
          check = true;
        }
      }

      if(check){
        productRepository.deleteById(product.getId());
      }
    }


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

  public void saveProductImageMetadata(String productId, List<String> fileNames){
    Product product = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    Set<String> fileNameSet = new HashSet<>(fileNames);
    product.setProductImages(fileNameSet);
    productRepository.save(product);
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
    System.out.println("here...............");
    for(FlashSaleProductResponse x : list){
      System.out.println(x.getProductName());
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
    System.out.println("ser vice goit");
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

  public void updateAllProductsWithDefaultAvailable() {
    List<Product> allProducts = productRepository.findAll();

    for (Product product : allProducts) {
      if (product.getViews() == null) {
        // product.setShippingId("683b529e2a9cfc41ae6f134b");
        // Map<Integer, Long> maps = new HashMap<>();
        // // maps.put(5,1L);

        // product.setRating(maps);
        // product.setSold(0L);
        // productRepository.save(product);
        product.setViews(0L);
      }
    }
  }

  public Page<ProductResponse> searchByProductName(String productName, int page, int size){
    // System.out.println("product name: " + productName);
    // Page<Product> list = productRepository.findByProductName(productName, PageRequest.of(page, size));
    // return list.map(product -> modelMapper.map(product, ProductResponse.class));

    String normalizedProductName = Normalizer.normalize(productName, Normalizer.Form.NFC);
    System.out.println("Normalized product name: " + normalizedProductName);
    Page<Product> list = productRepository.findByProductName(normalizedProductName, PageRequest.of(page, size));
    return list.map(product -> modelMapper.map(product, ProductResponse.class));
  }

  @PreAuthorize("hasAnyRole('ROLE_SHIPPER','ROLE_STAFF','ROLE_ADMIN')")
  public ProductResponse updateSoldById(String id, Long sold){
    Product product = productRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.PRODUCTID_INVALID));

    System.out.println("update sold: " + product.getSold() + sold);
    
    product.setSold(product.getSold() + sold);

    return productMapper.toProductResponse(productRepository.save(product));
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

    List<Product> first10 = mutableList.subList(0, Math.min(10, list.size()));

    return productMapper.toListProductResponse(first10);
  }
}
