package com.tranxuanphong.peterservice.configuration;

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

  };

  private final String[] PUBLIC_ENDPOINTS_GET = {
    "/api/flashsales/**",
    "/api/petercategories/**",
    "/api/vouchers/**",
    "/api/product-vouchers/**",
    "/api/seller-vouchers/**",
    "/api/shipping-vouchers/**",
    "/api/shippings/**",
    "/api/vouchers",
    "/api/vouchers/{id}",
    "/api/vouchers/id/{id}",
    "/api/flashsales/get/discount/flashsale/id/{flashsaleid}/product/id/{productid}",
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