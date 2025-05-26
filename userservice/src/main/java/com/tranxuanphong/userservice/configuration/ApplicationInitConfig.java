package com.tranxuanphong.userservice.configuration;


import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.entity.Role;
import com.tranxuanphong.userservice.entity.Permission;
import com.tranxuanphong.userservice.repository.RoleRepository;
import com.tranxuanphong.userservice.repository.UserRepository;

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
                Permission permission = Permission.builder()
                .name("ADD_TO_CART")
                .description("add to cart ...")
                .build();

                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);

                Role userRole = Role.builder()
                        .name("ROLE_USER")
                        .description("Standard user role")  
                        .permissionIds(permissions)                      
                        .build();
                roleRepository.save(userRole);
                roles.add(userRole);
            }

            if (roleRepository.findById("ROLE_STAFF").isEmpty()) {
                Permission permission = Permission.builder()
                .name("MANAGE_USER")
                .description("add to cart ...")
                .build();

                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);

                Role staffRole = Role.builder()
                        .name("ROLE_STAFF")
                        .description("Standard user role")  
                        .permissionIds(permissions)                      
                        .build();
                roleRepository.save(staffRole);
                roles.add(staffRole);
            }

            if (roleRepository.findById("ROLE_ADMIN").isEmpty()) {
                Permission permission = Permission.builder()
                .name("APPROVE_POST")
                .description("approve post ...")
                .build();

                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);

                Role adminRole = Role.builder()
                        .name("ROLE_ADMIN")
                        .description("Administrator role")
                        .permissionIds(permissions)
                        .build();
                roleRepository.save(adminRole);
                roles.add(adminRole);  
            }
       
            if(userRepository.findByEmail("phongtx.it@gmail.com").isEmpty()){                     
                User user = User.builder() 
                        .email("phongtx.it@gmail.com") 
                        .password(passwordEncoder.encode("adminphong"))     
                        .roles(roles)                
                        .build();

                userRepository.save(user); 
                log.warn("Default admin user created with default password admin, please change!");
            } 
        }; 
    } 
} 
