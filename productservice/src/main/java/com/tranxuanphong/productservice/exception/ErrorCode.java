package com.tranxuanphong.productservice.exception;

 
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor; 
import lombok.Getter; 
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults; 
 
@NoArgsConstructor 
@AllArgsConstructor 
@Getter 
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode { 
  UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception",  HttpStatus.INTERNAL_SERVER_ERROR), 
  INVALID_KEY(9998, "Invalid message key", HttpStatus.BAD_REQUEST),  
  UNAUTHENTICATED(1007, "User is not authenticated",  HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1006, "Unauthorized: you don't have permission", HttpStatus.FORBIDDEN),
  CATEGORY_EXISTS(1008,"category already exists", HttpStatus.BAD_REQUEST),
  SELLER_NOT_EXISTS(1009,"seller not exists", HttpStatus.BAD_REQUEST),
  EMAIL_NOT_EXISTED(1010, "Email not exists",  HttpStatus.NOT_FOUND),
  CATEGORY_NOT_EXISTS(1011, "category not exists",  HttpStatus.NOT_FOUND),
  PETER_CATEGORY_NOT_EXISTS(1011, "category not exists",  HttpStatus.NOT_FOUND),
  PRODUCT_EXISTS(1012,"product already exists", HttpStatus.BAD_REQUEST),
  PRODUCT_NOT_EXISTS(1013,"product not exists", HttpStatus.NOT_FOUND),
  PRODUCT_INVALID(1014,"PRODUCT NAME INVALID", HttpStatus.BAD_REQUEST),
  SELLERID_INVALID(1015,"SELLER ID INVALID", HttpStatus.BAD_REQUEST),
  CATEGORYID_INVALID(1015,"category INVALID", HttpStatus.BAD_REQUEST),
  DESCRIPTION_INVALID(1015,"SELLER ID INVALID", HttpStatus.BAD_REQUEST),
  PRODUCTID_INVALID(1015,"PRODUCT ID INVALID", HttpStatus.NOT_FOUND),
  INFO_NAME_INVALID(1015,"INFO NAME INVALID", HttpStatus.BAD_REQUEST),
  INFO_DETAIL_INVALID(1015,"INFO DETAIL INVALID", HttpStatus.BAD_REQUEST),
  INFO_NOT_EXISTS(1013,"info not exists", HttpStatus.NOT_FOUND),
  INFO_EXISTS(1013,"info already exists", HttpStatus.BAD_REQUEST),
  VARIANT_NAME_INVALID(1013,"  VARIANT NAME INVALID", HttpStatus.BAD_REQUEST),
  PRICE_INVALID(1013,"  VARIANT PRICE INVALID", HttpStatus.BAD_REQUEST),
  STOCK_INVALID(1013,"  VARIANT STOCK INVALID", HttpStatus.BAD_REQUEST),
  VARIANT_NOT_EXISTS(1013,"variant not exists", HttpStatus.NOT_FOUND),
  VARIANT_EXISTS(1013,"variant already exists", HttpStatus.BAD_REQUEST),

  CATEGORY_SELLER_NOT_MATCH(1013,"CATEGORY NOT BELONG TO SELLER", HttpStatus.BAD_REQUEST),
  PRODUCT_SELLER_NOT_MATCH(1013,"CATEGORY NOT BELONG TO SELLER", HttpStatus.BAD_REQUEST),
  PRODUCT_IN_CATEGORY_EXIST(1013,"products may exist in this category", HttpStatus.BAD_REQUEST),
  
  INFO_NAME_DUPLICATE(1013,"info name duplicate ...", HttpStatus.BAD_REQUEST),
  SELLER_NOT_EXIST(1013,"seller not exists ...", HttpStatus.NOT_FOUND),
  SELLER_EXIST(1013,"seller already exists ...", HttpStatus.BAD_REQUEST),
  INVALID_RATING(1013,"rating from 1 to 5 ...", HttpStatus.BAD_REQUEST),
  ; 

  int code; 
  String message; 
  HttpStatusCode statusCode;
} 
