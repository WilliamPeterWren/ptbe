package com.tranxuanphong.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// import org.springframework.context.annotation.Bean;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.reactive.CorsWebFilter;


// import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

// import java.util.Arrays;


@SpringBootApplication
@EnableDiscoveryClient
public class ApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}

	// @Bean
	// public CorsWebFilter corsWebFilter() {
	// 	CorsConfiguration corsConfig = new CorsConfiguration();
	// 	corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://your-frontend-domain.com")); 
	// 	corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
	// 	corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));
	// 	corsConfig.setAllowCredentials(true); 
	// 	corsConfig.setMaxAge(3600L); 

	// 	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	// 	source.registerCorsConfiguration("/**", corsConfig); 

	// 	return new CorsWebFilter(source);
	// }

}
