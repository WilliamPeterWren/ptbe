package com.tranxuanphong.userservice.exception;

 
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
  EMAIL_EXISTED(1001, "Email already exists",  HttpStatus.BAD_REQUEST), 
  EMAIL_INVALID(1003, "Email must have @ characters",  HttpStatus.BAD_REQUEST), 
  PASSWORD_INVALID(1004, "Password must be at least {min} characters",  HttpStatus.BAD_REQUEST), 
  EMAIL_NOT_EXISTED(1006, "Email not exists",  HttpStatus.NOT_FOUND), 
  UNAUTHENTICATED(1007, "User is not authenticated",  HttpStatus.UNAUTHORIZED),
  UNAUTHORIZED(1006, "Unauthorized: you don't have permission", HttpStatus.FORBIDDEN),
  FIRSTNAME_INVALID(1008, "First name must be at least {min} characters", HttpStatus.BAD_REQUEST), 
  LASTNAME_INVALID(1009, "Last name must be at least {min} characters", HttpStatus.BAD_REQUEST),
  PERMISSION_NAME_INVALID(1010, "Permission name must be at least {min} characters", HttpStatus.BAD_REQUEST),
  PERMISSION_DESCRIPTION_INVALID(1011, "Permission description must be at least {min} characters", HttpStatus.BAD_REQUEST),
  ROLE_NAME_INVALID(1012, "Role name must be at least {min} characters", HttpStatus.BAD_REQUEST),
  ROLE_DESCRIPTION_INVALID(1013, "Role description must be at least {min} characters", HttpStatus.BAD_REQUEST),
  ROLE_NOT_FOUND(1014, "Role not found ", HttpStatus.NOT_FOUND),
  INVALID_DOB(1015,"You must be at least {min}", HttpStatus.BAD_REQUEST),
  ADDRESS_INVALID(1016,"ADDRESS must be at least {min}", HttpStatus.BAD_REQUEST),
  INVALID_PHONE(1017,"Phone must be 10 characters", HttpStatus.BAD_REQUEST),
  CATEGORY_EXISTS(1018,"category exists",HttpStatus.BAD_REQUEST ),
  USER_ADDRESS_NOT_MATCH(1018,"USER address conflic",HttpStatus.BAD_REQUEST ),
  EMAIL_NOT_VERIFY(1018,"user is not verified",HttpStatus.BAD_REQUEST ),
  VOUCHER_NOT_FOUND(1018,"voucher not found",HttpStatus.NOT_FOUND ),
  VOUCHER_ALREADY_USED(1018,"voucher not found",HttpStatus.BAD_REQUEST ),
  ; 

  int code; 
  String message; 
  HttpStatusCode statusCode;
} 
