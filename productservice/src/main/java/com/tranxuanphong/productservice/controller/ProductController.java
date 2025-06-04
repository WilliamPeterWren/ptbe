package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.request.ProductCreateRequest;
import com.tranxuanphong.productservice.dto.request.ProductUpdateRequest;
import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.CartProductResponse;
import com.tranxuanphong.productservice.dto.response.FlashSaleProductResponse;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.service.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  ProductService productService;


  @PostMapping
  public ApiResponse<ProductResponse> create(@RequestBody ProductCreateRequest request) {
    return ApiResponse.<ProductResponse>builder()
      .result(productService.create(request))
      .build();
  }

  @GetMapping("/get-products")
  public ApiResponse<Page<ProductResponse>> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.<Page<ProductResponse>>builder()
    .result(productService.getPaginatedProducts(page, size))
    .build();
  }

  @GetMapping("/slug/{slug}")
  public ApiResponse<ProductResponse> getOneBySlug(@PathVariable String slug) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.getOneBySlug(slug))
    .build();
  }

  @GetMapping("/id/{id}")
  public ApiResponse<ProductResponse> getOne(@PathVariable String id) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.getOneById(id))
    .build();
  }

  @PutMapping("/{id}")
  public ApiResponse<ProductResponse> updateById(@PathVariable String id, @RequestBody ProductUpdateRequest request) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.updateById(id, request))
    .build();
  }
  
  @DeleteMapping("/{id}")
  public ApiResponse<ProductResponse> delete(@PathVariable String id){
    return ApiResponse.<ProductResponse>builder()
    .result(productService.delete(id))
    .build();
  }

  @GetMapping("/check/product/id/{id}")
  public boolean checkProductId(@PathVariable String id) {
      return productService.checkProductId(id);
  }

  @GetMapping("/check/product/{productId}/seller/{sellerId}")
  public boolean checkProductBySellerId(@PathVariable String productId, @PathVariable String sellerId) {
    return productService.checkProductBySellerId(productId, sellerId);
  }

  @GetMapping("/get-products/seller/{sellerId}")
  public ApiResponse<Page<ProductResponse>> getProductsBySeller(@PathVariable String sellerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.<Page<ProductResponse>>builder()
    .result(productService.getPaginatedProductsBySellerId(sellerId, page, size))
    .build();
  }
  
  @GetMapping("/check/variant/id/{id}")
  public boolean doesVariantExist(@PathVariable String id) {
    return productService.doesVariantExist(id);
  }

  @GetMapping("/check/variant/id/{variantId}/seller/id/{sellerId}")
  public boolean doesVariantExistBySellerId(@PathVariable String variantId, @PathVariable String sellerId) {
    return productService.doesVariantExistBySellerId(variantId, sellerId);
  }

  @GetMapping("/get/product/images/id/{id}")
  public List<String> getProductImageMetadata(@PathVariable String id) {
    return productService.getProductImageMetadata(id);
  }

  @PostMapping("/set/product/images/id/{id}")
  public void setProductImageMetadata(@PathVariable String id, @RequestBody List<String> fileNames) {
    productService.saveProductImageMetadata(id, fileNames);
  }
  
  @PostMapping("/get/products/by/ids")
  public List<FlashSaleProductResponse> getProductImageMetadata(@RequestBody List<String> ids) {
    return productService.getListProductByIds(ids);
  }

  @GetMapping("/get/product/rand/limit/{limit}")
  public List<ProductResponse> getRandomProducts(@PathVariable int limit) {
    return productService.getRandomProducts(limit);
  }

  @GetMapping("/get/product/peter/{peterCategoryId}")
  public Page<ProductResponse> getRandomProducts(@PathVariable String peterCategoryId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return productService.getProductByPeterCategory(peterCategoryId, page, size);
  }


  @GetMapping("/get/product/variant/id/{id}")
  public ProductResponse findProductByVariantId(@PathVariable String id) {
    
    return productService.findProductByVariantId(id);
  }

  @GetMapping("/get/cartproduct/variant/id/{id}")
  public CartProductResponse getCartProductResponse(@PathVariable String id) {
    System.out.println("id ..........." + id);
    return productService.cartProductResponse(id);
  }

  @DeleteMapping("/delete/admin/product")
  public void deleteByAdmin(){
    productService.deleteByAdmin();
  }
  

  // @PostMapping("/update/all/shipping")
  // public void postMethodName(@RequestBody String entity) {
  //   productService.updateAllProductsWithDefaultAvailable();
  // }

  // @PostMapping("/update/all/rating")
  // public void postMethodName() {
  //   productService.updateAllProductsWithDefaultAvailable();
  // }

  @GetMapping("/search/product/productname/{productname}")
  public Page<ProductResponse> searchProductByProductName(@PathVariable String productname,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        String decodedProductName = URLDecoder.decode(productname, StandardCharsets.UTF_8);
        return productService.searchByProductName(decodedProductName, page, size);
    }


  @PutMapping("/id/{id}/sold/{sold}")
  public ApiResponse<ProductResponse> updateSoldById(@PathVariable String id, @PathVariable Long sold) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.updateSoldById(id, sold))
    .build();
  }


  @PutMapping("/rating/{rating}/product/id/{id}")
  public ApiResponse<ProductResponse> updateRatingById(@PathVariable String id, @PathVariable Integer rating) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.updateRatingById(id, rating))
    .build();
  }
}


  
  


