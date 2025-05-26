package com.tranxuanphong.productservice.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.productservice.dto.request.ProductCreateRequest;
import com.tranxuanphong.productservice.dto.request.ProductUpdateRequest;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.entity.Product;

// import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring") 
public interface ProductMapper {
  Product toProduct(ProductCreateRequest request); 
  Product updateProduct(@MappingTarget Product product, ProductUpdateRequest request); 
  ProductResponse toProductResponse(Product product); 
  List<ProductResponse> toListProductResponse(List<Product> listProduct);
}
