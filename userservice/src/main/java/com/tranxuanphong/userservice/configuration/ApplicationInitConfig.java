package com.tranxuanphong.userservice.configuration;


import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.entity.Role;
import com.tranxuanphong.userservice.repository.mongo.RoleRepository;
import com.tranxuanphong.userservice.repository.mongo.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Configuration 
@RequiredArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) 
public class ApplicationInitConfig { 
 
    PasswordEncoder passwordEncoder; 
 
    @Bean 
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) { 
        return args -> {            

            Set<Role> roles = new HashSet<>();

            if (roleRepository.findById("ROLE_USER").isEmpty()) {
                Role userRole = Role.builder()
                        .name("ROLE_USER")
                        .description("Standard user role")  
                        .build();
                roleRepository.save(userRole);
                roles.add(userRole);
            }

            if (roleRepository.findById("ROLE_STAFF").isEmpty()) {
                Role staffRole = Role.builder()
                        .name("ROLE_STAFF")
                        .description("Standard staff role")  
                        .build();
                roleRepository.save(staffRole);
                roles.add(staffRole);
            }

            if (roleRepository.findById("ROLE_SELLER").isEmpty()) {
                Role staffRole = Role.builder()
                        .name("ROLE_SELLER")
                        .description("Standard seller role")  
                        .build();
                roleRepository.save(staffRole);
                roles.add(staffRole);
            }

            if (roleRepository.findById("ROLE_ADMIN").isEmpty()) {       
                Role adminRole = Role.builder()
                        .name("ROLE_ADMIN")
                        .description("Administrator role")
                        .build();
                roleRepository.save(adminRole);
                roles.add(adminRole);  
            }
       
            if(userRepository.findByEmail("phongtx.it@gmail.com").isEmpty()){                     
                User user = User.builder() 
                        .email("phongtx.it@gmail.com")
                        .username("admin")
                        .password(passwordEncoder.encode("adminphong"))     
                        .roles(roles)                
                        .build();

                userRepository.save(user); 
                log.warn("Default admin user created with default password admin, please change!");
            } 
        }; 
    } 
} 
