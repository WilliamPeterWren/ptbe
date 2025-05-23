package com.tranxuanphong.productservice.exception;

import lombok.Getter; 
import lombok.NoArgsConstructor; 
import lombok.Setter; 
 
@Setter 
@Getter 
@NoArgsConstructor 
public class AppException extends RuntimeException { 
    private ErrorCode errorCode; 
 
    public AppException(ErrorCode errorCode) { 
        super(errorCode.getMessage()); 
        this.errorCode = errorCode; 
    } 
} 
