package com.tranxuanphong.productservice.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final String[] PUBLIC_ENDPOINTS_POST = {
    // "/api/users/register", 
    "/api/products/set/product/images/id/{id}",
    "/api/products/get/products/by/ids",

    "/api/products/update/views/slug/{slug}",
    "/api/reviews/check/reviews/user/product/variant",

  };

  private final String[] PUBLIC_ENDPOINTS_GET = {

    "/api/products/slug/{slug}",
    "/api/products/id/{id}",

    "/api/categories/{slug}",
    "/api/categories/get-by-sellerid/{sellerId}",

    "/api/products/product-by-seller/{productId}/{sellerId}",

    "/api/products/get-products/seller/{sellerId}",
    "/api/products/get/product/images/id/{id}",
    "/api/products/get/products/by/ids",
    "/api/products/get/product/rand/limit/{limit}",
    "/api/products/get/product/peter/{peterCategoryId}",
    "/api/products/get/product/variant/id/{id}",
    "/api/products/get/cartproduct/variant/id/{id}",
    "/api/products/count/seller/{sellerId}",
    "/api/products/rand/seller/id/{sellerId}/limit/{limit}",
    "/api/products/seller/id/{sellerId}/category/id/{categoryId}",
    
    "/api/products/check/product/{productId}/seller/{sellerId}",
    "/api/products/check/variant/id/{id}",
    "/api/products/check/**",
    "/api/products/search/product/productname/{productname}",

    "/api/reviews/get/reviews/product/id/{id}",
    "/api/products/get/seller/id/product/id/{id}",
    "/api/products/product/id/{id}",
  };

  // private String signerKey = "WjG25z4tA+dZX3clK+u0/kRg1tCdV6vvaizOfsJLgy4HFasdkfwxKso+KlGArOm4uOdkai";
  
  @Value("${jwt.signerKey}")
  private String signerKey;
  
  @Bean 
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception { 
    httpSecurity.authorizeHttpRequests(
      requests -> requests.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()
            // .requestMatchers(HttpMethod.GET, "/api/users/get-all").hasRole(Role.ADMIN.name()) // no need
              .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()
            .anyRequest().authenticated() 
            // .anyRequest().permitAll()
    ); 

    // httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))); 

    httpSecurity.oauth2ResourceServer(oauth2 -> 
                oauth2.jwt(jwtConfigurer ->  
                    jwtConfigurer.decoder(jwtDecoder()) 
                                 .jwtAuthenticationConverter(jwtAuthenticationConverter())) 
                      .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) 
                       
        ); 

    httpSecurity.csrf(AbstractHttpConfigurer::disable); 

    return httpSecurity.build(); 
  } 

   JwtAuthenticationConverter jwtAuthenticationConverter() { 
      JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
      jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
      jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); 
      JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter(); 
      jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter); 
      return jwtAuthenticationConverter; 
    } 
 
  @Bean 
  JwtDecoder jwtDecoder() { 
    SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512"); 
    return NimbusJwtDecoder.withSecretKey(secretKeySpec) 
      .macAlgorithm(MacAlgorithm.HS512) 
      .build(); 
  }

  @Bean 
  PasswordEncoder passwordEncoder() { 
    return new BCryptPasswordEncoder(10); 
  }
}