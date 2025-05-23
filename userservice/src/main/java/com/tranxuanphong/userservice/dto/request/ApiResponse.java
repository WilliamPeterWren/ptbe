package com.tranxuanphong.userservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults; 
 
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) 
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> { 
  int code; 
  String message; 
  T result; 
}