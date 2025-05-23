package com.tranxuanphong.productservice.utils;

import com.github.slugify.Slugify;
import org.springframework.stereotype.Component;

@Component
public class GenerateUnidecode {
  private final Slugify slugify;

  public GenerateUnidecode() {
    this.slugify = Slugify.builder().build();
  }

  public String unidecode(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Input string cannot be null");
    }
    return slugify.slugify(input);
  }
}