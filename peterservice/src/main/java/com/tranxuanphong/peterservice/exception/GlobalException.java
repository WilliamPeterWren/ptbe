package com.tranxuanphong.peterservice.exception;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tranxuanphong.peterservice.dto.response.ApiResponse;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalException {

  private static final String MIN_ATTRIBUTES = "min";
  
  @ExceptionHandler(RuntimeException.class)
  ResponseEntity<ApiResponse<?>> handlingRuntimeException(RuntimeException exception){
    ApiResponse<?> apiResponse = new ApiResponse<>();
    apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

    return ResponseEntity.badRequest().body(apiResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiResponse<?>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception){
    String enumKey = exception.getFieldError().getDefaultMessage(); 
    // System.out.println("enumKey: " + enumKey); 
    ErrorCode errorCode = ErrorCode.INVALID_KEY; 

    Map<String, Object> attributes = null;

    try { 
      errorCode = ErrorCode.valueOf(enumKey); 
      var constrainViolation = exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

      attributes = constrainViolation.getConstraintDescriptor().getAttributes();
      log.info(attributes.toString());
    } catch (IllegalArgumentException e) { 
      // System.out.println(e.getMessage()); 
    } 

    ApiResponse<?> apiResponse = new ApiResponse<>(); 
    apiResponse.setCode(errorCode.getCode()); 
    apiResponse.setMessage(Objects.nonNull(attributes) ? 
     mapAttributes(errorCode.getMessage(), attributes) :
      errorCode.getMessage()
    ); 

    return ResponseEntity.badRequest().body(apiResponse); 
  }

  @ExceptionHandler(value = AppException.class) 
  ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) { 
    ErrorCode errorCode = exception.getErrorCode(); 

    ApiResponse<?> apiResponse = new ApiResponse<>(); 
    apiResponse.setCode(errorCode.getCode()); 
    apiResponse.setMessage(errorCode.getMessage()); 

    return ResponseEntity.badRequest().body(apiResponse); 
  }

  // @ExceptionHandler(value = AccessDeniedException.class)
  // ResponseEntity<ApiResponse<?>> handlingAccessDeniedException(AccessDeniedException exception) {
  //   ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
  //   return ResponseEntity.status(errorCode.getStatusCode())
  //     .body(ApiResponse.builder()
  //     .code(errorCode.getCode())
  //     .message(errorCode.getMessage())
  //     .build());
  // }

  private String mapAttributes(String message, Map<String, Object> attributes){
    String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTES));

    return message.replace("{" + MIN_ATTRIBUTES + "}", minValue);
  }
}
