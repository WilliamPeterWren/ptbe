package com.tranxuanphong.userservice.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import com.tranxuanphong.userservice.dto.request.AuthenticationRequest;
import com.tranxuanphong.userservice.dto.request.IntrospectRequest;
import com.tranxuanphong.userservice.dto.response.AuthenticationResponse;
import com.tranxuanphong.userservice.dto.response.IntrospectResponse;
import com.tranxuanphong.userservice.entity.User;
import com.tranxuanphong.userservice.exception.AppException;
import com.tranxuanphong.userservice.exception.ErrorCode;
import com.tranxuanphong.userservice.repository.mongo.UserRepository;

import lombok.RequiredArgsConstructor;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) 
@Slf4j
public class AuthenticationService { 
    UserRepository userRepository; 

    @NonFinal 
    @Value("${jwt.signerKey}") 
    protected String SIGNER_KEY; 

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){ 
        User user = userRepository.findByEmail(authenticationRequest.getEmail()) 
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)) ; 
 
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); 
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), 
user.getPassword()); 
 
        if(!authenticated){ 
            throw new AppException(ErrorCode.UNAUTHENTICATED); 
        } 
 
        var token = generateToken(authenticationRequest.getEmail()); 
 
        new AuthenticationResponse(); 
        return AuthenticationResponse.builder() 
                .token(token) 
                .authenticated(true) 
                .build(); 
    } 
 
    public String generateToken(String email) { 
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512); 
 
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder() 
                .subject(email) 
                .issuer("tranxuanphong.com") 
                .issueTime(new Date()) 
                .expirationTime(new Date( 
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() 
                )) 
                .claim("userId", "Custom") 
                .build(); 
 
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); 
 
        JWSObject jwsObject = new JWSObject(jwsHeader, payload); 
 
        try { 
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); 
            return jwsObject.serialize(); 
        } catch (JOSEException e) { 
            log.error("Can't create token: {}", e.getMessage()); 
            throw new RuntimeException(e); 
        } 
    } 

    public String generateAccessToken(String email, Set<String> roles, String id) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("tranxuanphong.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("userId", id)
                .claim("email", email)
                .claim("roles", roles)
              
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can't create token: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(String email, String id) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("tranxuanphong.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("userId", id)
                .claim("email", email)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can't create refresh token: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws 
JOSEException, ParseException { 
        String token = introspectRequest.getToken(); 
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes()); 
        SignedJWT signedJWT = SignedJWT.parse(token); 
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime(); 
        boolean verified = signedJWT.verify(jwsVerifier); 
        return IntrospectResponse.builder() 
                .valid(verified && expirationDate.after(new Date())) 
                .build(); 
    }
} 