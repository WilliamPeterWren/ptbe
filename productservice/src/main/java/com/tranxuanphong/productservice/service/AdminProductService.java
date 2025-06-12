package com.tranxuanphong.productservice.service;

import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.entity.Variant;
import com.tranxuanphong.productservice.repository.mongo.ProductRepository;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.modelmapper.ModelMapper;


import java.util.List;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AdminProductService {
  ProductRepository productRepository;

  ModelMapper modelMapper;

  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
  public Page<ProductResponse> getPaginatedProducts(int page, int size) {    
    Page<Product> list = productRepository.findAll(PageRequest.of(page, size));
    return list.map(product -> modelMapper.map(product, ProductResponse.class));
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
}
