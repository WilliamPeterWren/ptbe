package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  ProductService productService;

  @GetMapping("/slug/{slug}")
  public ApiResponse<ProductResponse> getOneBySlug(
          @PathVariable String slug,
          @RequestParam(required = false) String flashsaleId) {
      
    return ApiResponse.<ProductResponse>builder()
              .result(productService.getOneBySlug(flashsaleId, slug))
              .build();
  }
  

  @GetMapping("/id/{id}")
  public ApiResponse<ProductResponse> getOne(@PathVariable String id) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.getOneById(id))
    .build();
  }

  @GetMapping("/product/id/{id}")
  public ProductResponse getProductResponseByProductId(@PathVariable String id) {
    return productService.getOneById(id);
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
    System.out.println("controller");
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
    return productService.cartProductResponse(id);
  }

  @GetMapping("/search/product/productname/{productname}")
  public Page<ProductResponse> searchProductByProductName(@PathVariable String productname,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
    String decodedProductName = URLDecoder.decode(productname, StandardCharsets.UTF_8);
    return productService.searchByProductName(decodedProductName, page, size);
  }

  @PutMapping("/rating/{rating}/product/id/{id}")
  public ApiResponse<ProductResponse> updateRatingById(@PathVariable String id, @PathVariable Integer rating) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.updateRatingById(id, rating))
    .build();
  }

  @GetMapping("/count/seller/{sellerId}")
  public int countProductBySellerId(@PathVariable String sellerId) {
    return productService.countProductBySellerId(sellerId);
  }

  @PostMapping("/update/views/id/{id}")
  public void updateProductViews(@PathVariable String id) {
    productService.updateProductViews(id);
  }

  @PostMapping("/update/views/slug/{slug}")
  public void updateProductViewsBySLug(@PathVariable String slug) {
    productService.updateProductViewsBySLug(slug);
  }
  
  @GetMapping("/rand/seller/id/{sellerId}/limit/{limit}")
  public ApiResponse<List<ProductResponse>> getRandomProductBySellerId(@PathVariable String sellerId, @PathVariable int limit) {
    return ApiResponse.<List<ProductResponse>>builder()
    .result(productService.getRandomProductBySellerId(sellerId, limit))
    .build();
  }
  
  @GetMapping("/seller/id/{sellerId}/category/id/{categoryId}")
  public ApiResponse<Page<ProductResponse>> findBySellerIdAndCategoryId(@PathVariable String sellerId, @PathVariable String categoryId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.<Page<ProductResponse>>builder()
    .result(productService.findBySellerIdAndCategoryId(sellerId, categoryId, page, size))
    .build();
  }

  @GetMapping("/get/seller/id/product/id/{id}")
  public String getSellerIdByProductId(@PathVariable String id) {
    return productService.getSellerIdByProductId(id);
  }
  
}

