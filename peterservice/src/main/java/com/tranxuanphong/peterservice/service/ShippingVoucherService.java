package com.tranxuanphong.peterservice.service;

import com.tranxuanphong.peterservice.entity.ShippingVoucher;
import com.tranxuanphong.peterservice.exception.AppException;
import com.tranxuanphong.peterservice.exception.ErrorCode;
import com.tranxuanphong.peterservice.repository.httpclient.UserClient;
import com.tranxuanphong.peterservice.repository.mongo.ShippingVoucherRepository;
import com.tranxuanphong.peterservice.service.ShippingVoucherService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingVoucherService {

    ShippingVoucherRepository shippingVoucherRepository;

    UserClient userClient;

    public List<ShippingVoucher> getAll() {
        return shippingVoucherRepository.findAll();
    }

    public List<ShippingVoucher> userGetAll() {
        return shippingVoucherRepository.findByAvailableIsTrueAndExpiredAtAfter(Instant.now());
    }

    public Optional<ShippingVoucher> getById(String id) {
        return shippingVoucherRepository.findById(id);
    }

    
    public ShippingVoucher getByIdd(String id) {
        return shippingVoucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public ShippingVoucher create(ShippingVoucher voucher, int count) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authToken = null;
        if (authentication != null && authentication.getCredentials() instanceof String) {
          authToken = (String) authentication.getCredentials();
        } else if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
          authToken = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal()).getTokenValue();
        }
    
        if (authToken == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String authorizationHeader = "Bearer " + authToken;

        ShippingVoucher shippingVoucher = shippingVoucherRepository.save(voucher);
        userClient.updateShippingVoucher(shippingVoucher.getId(), count, authorizationHeader);

        return shippingVoucher;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public ShippingVoucher update(String id, int count, ShippingVoucher updateData) {
//        if (!repository.existsById(id)) {
//            throw new RuntimeException("ShippingVoucher not found");
//        }
//        voucher.setId(id);
        ShippingVoucher shippingVoucher = shippingVoucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHIPPING_INVALID));

        boolean checkUpdate = false;

        if(updateData.getName() !=null ){
            shippingVoucher.setName(updateData.getName());
            checkUpdate = true;
        }

        if(updateData.getExpiredAt()!=null && updateData.getExpiredAt().isAfter(Instant.now())){
            shippingVoucher.setExpiredAt(updateData.getExpiredAt());
        }

        if(updateData.getPrice()!=null && updateData.getPrice() > 5000L){
            shippingVoucher.setPrice(updateData.getPrice());
            checkUpdate = true;
        }
        System.out.println("avai: " + updateData.getAvailable());
        if(updateData.getAvailable() != null){

            shippingVoucher.setAvailable(updateData.getAvailable());
            checkUpdate = true;
        }

        if(checkUpdate){
            shippingVoucher.setUpdatedAt(Instant.now());
        }

        shippingVoucherRepository.save(shippingVoucher);

        if(count <=0 ){
            return shippingVoucher;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authToken = null;
        if (authentication != null && authentication.getCredentials() instanceof String) {
          authToken = (String) authentication.getCredentials();
        } else if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
          authToken = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal()).getTokenValue();
        }
    
        if (authToken == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String authorizationHeader = "Bearer " + authToken;

        userClient.updateShippingVoucher(shippingVoucher.getId(), count, authorizationHeader);

        return shippingVoucher;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF')")
    public void delete(String id) {
//        repository.deleteById(id);
       ShippingVoucher shippingVoucher = shippingVoucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
       shippingVoucher.setAvailable(false);
       shippingVoucherRepository.save(shippingVoucher);
    }

    public void adminUpdateAll(){
        List<ShippingVoucher> list = shippingVoucherRepository.findAll();
        for(ShippingVoucher s : list){
//            s.setAvailable(true);
//            s.setCreatedAt(Instant.now());
//            s.setUpdatedAt(Instant.now());

//            repository.save(s);
        }
    }
}
