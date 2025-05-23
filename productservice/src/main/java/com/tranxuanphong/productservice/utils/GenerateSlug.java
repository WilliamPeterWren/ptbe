package com.tranxuanphong.productservice.utils;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class GenerateSlug {
  GenerateUnidecode generateUnidecode;
  GenerateUUID generateUUID;

  public String generateSlug(String input){
    return generateUnidecode.unidecode(input) + "-" + generateUUID.generateUuid();
  }
}
