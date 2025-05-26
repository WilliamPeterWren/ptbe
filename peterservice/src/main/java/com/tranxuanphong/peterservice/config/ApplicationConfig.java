// src/main/java/com/tranxuanphong/peterservice/config/ApplicationConfig.java
package com.tranxuanphong.peterservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class ApplicationConfig {

    @Bean 
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}