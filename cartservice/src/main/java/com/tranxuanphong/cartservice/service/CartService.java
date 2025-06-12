package com.tranxuanphong.cartservice.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.cartservice.dto.model.ItemResponse;
import com.tranxuanphong.cartservice.dto.model.SellerResponse;
import com.tranxuanphong.cartservice.dto.request.AddToCartRequest;
import com.tranxuanphong.cartservice.dto.request.CartUpdateRequest;
import com.tranxuanphong.cartservice.dto.response.CartResponse;
import com.tranxuanphong.cartservice.entity.Cart;
import com.tranxuanphong.cartservice.entity.Item;
import com.tranxuanphong.cartservice.entity.Seller;
import com.tranxuanphong.cartservice.exception.AppException;
import com.tranxuanphong.cartservice.exception.ErrorCode;
import com.tranxuanphong.cartservice.repository.CartRepository;
import com.tranxuanphong.cartservice.repository.httpclient.PeterClient;
import com.tranxuanphong.cartservice.repository.httpclient.ProductClient;
import com.tranxuanphong.cartservice.repository.httpclient.UserClient;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class CartService {
  CartRepository cartRepository;

  UserClient userClient;
  ProductClient productClient;
  PeterClient peterClient;

  
  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse create(){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String userId = userClient.userId(email);

    Cart cart = Cart.builder()
    .userId(userId)
    .build();

    cartRepository.save(cart);

    return CartResponse.builder()
    .build();

  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public Set<SellerResponse> getCart(){

    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<SellerResponse> cartSellers = new HashSet<>();
    Set<Seller> sellers = cart.getSellers();

    for(Seller seller : sellers){

      String sellerId = seller.getSellerId();
      String sellerUsername = userClient.username(sellerId);
      
      Set<ItemResponse> itemResponse = new HashSet<>();
      Set<Item> cartItems = seller.getItems();

      for(Item cartItem : cartItems){
    
        ItemResponse cartProduct = productClient.getProductByVariantId(cartItem.getVariantId());

        String latestFlashsaleId = "";
        try {
          latestFlashsaleId = peterClient.getLastestFlashSalesId();
        } catch (Exception e) {
          System.out.println("error: " + e.getMessage());
        }

        Long discount = 0L;
        try {
          discount = peterClient.getDiscountByFlashSaleIdAndProductId(latestFlashsaleId, cartProduct.getProductId());
        } catch (Exception e) {
          System.out.println("error: " + e.getMessage());
        }

        cartProduct.setQuantity(cartItem.getQuantity());
        cartProduct.setUpdatedAt(cartItem.getUpdatedAt());
        cartProduct.setDiscount(discount);

        itemResponse.add(cartProduct);
      }


      SellerResponse cartSeller = SellerResponse.builder()
      .sellerId(sellerId)
      .sellerUsername(sellerUsername)
      .itemResponses(itemResponse)
      .updatedAt(seller.getUpdatedAt())
      .build();

      cartSellers.add(cartSeller);
    }


    return cartSellers;
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public Set<SellerResponse> addToCart(AddToCartRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    // if(request.getVariantId() != null && !productClient.doesVariantExistById(request.getVariantId())){
    //   throw new AppException(ErrorCode.VARIANT_NOT_EXISTS);
    // }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<SellerResponse> sellerResponses = new HashSet<>();
    Set<Seller> sellers = cart.getSellers();
    
    boolean sellerIdCheck = false;
    boolean variantCheck = false;

    for(Seller seller : sellers){

      String sellerId = seller.getSellerId();

      if(sellerId.equals(request.getSellerId())){
        sellerIdCheck = true;
      }

      String sellerUsername = userClient.username(sellerId);

      Set<ItemResponse> itemResponses = new HashSet<>();
      Set<Item> items = seller.getItems();


      for(Item item : items){
        if(item.getVariantId().equals(request.getVariantId())){
          item.setQuantity(item.getQuantity() + request.getQuantity());    
          item.setUpdatedAt(Instant.now());
          seller.setUpdatedAt(Instant.now());
          
          seller.setItems(items);
          cart.setSellers(sellers);
          cartRepository.save(cart);

          variantCheck = true;          
        } 

        
        ItemResponse itemResponse = productClient.getProductByVariantId(item.getVariantId());
        itemResponse.setQuantity(item.getQuantity());      
        itemResponse.setUpdatedAt(item.getUpdatedAt());

        String latestFlashsaleId = "";
        try {
          latestFlashsaleId = peterClient.getLastestFlashSalesId();
        } catch (Exception e) {
          System.out.println("error: " + e.getMessage());
        }

        Long discount = 0L;
        try {
          discount = peterClient.getDiscountByFlashSaleIdAndProductId(latestFlashsaleId, itemResponse.getProductId());
        } catch (Exception e) {
          System.out.println("error: " + e.getMessage());
        }
        itemResponse.setDiscount(discount);

        itemResponses.add(itemResponse);
      }


      SellerResponse sellerResponse = SellerResponse.builder()
        .sellerId(sellerId)
        .sellerUsername(sellerUsername)
        .itemResponses(itemResponses)
        .updatedAt(seller.getUpdatedAt())
        .build();

        sellerResponses.add(sellerResponse);
    }
    


    if(!variantCheck){
      if(sellerIdCheck){

        // data base
        for(Seller seller : sellers){
          if(seller.getSellerId().equals(request.getSellerId())){

            // db
            Set<Item> items = seller.getItems();

            Item item = Item.builder()
            .variantId(request.getVariantId())
            .quantity(request.getQuantity())
            .build();

            items.add(item);
            seller.setItems(items);
            cart.setSellers(sellers);

            cartRepository.save(cart);
        
          }
        } 

        // response
        for(SellerResponse sellerResponse : sellerResponses){
          if(sellerResponse.getSellerId().equals(request.getSellerId())){
            Set<ItemResponse> itemResponses = sellerResponse.getItemResponses();

            ItemResponse itemResponse = productClient.getProductByVariantId(request.getVariantId());
            
            itemResponse.setQuantity(request.getQuantity());      
            itemResponse.setUpdatedAt(Instant.now());

            String latestFlashsaleId = "";
            try {
              latestFlashsaleId = peterClient.getLastestFlashSalesId();
            } catch (Exception e) {
              System.out.println("error: " + e.getMessage());
            }

            Long discount = 0L;
            try {
              discount = peterClient.getDiscountByFlashSaleIdAndProductId(latestFlashsaleId, itemResponse.getProductId());
            } catch (Exception e) {
              System.out.println("error: " + e.getMessage());
            }
            itemResponse.setDiscount(discount);

            itemResponses.add(itemResponse);

            sellerResponse.setItemResponses(itemResponses);

            return sellerResponses;

          }
        }
      
      } else {

        Set<Item> items = new HashSet<>();
        
        Item item = Item.builder()
        .variantId(request.getVariantId())
        .quantity(request.getQuantity())
        .build();

        items.add(item);

        Seller seller = Seller.builder()
        .sellerId(request.getSellerId())
        .items(items)
        .build();

        sellers.add(seller);
        cart.setSellers(sellers);

        cartRepository.save(cart);

        
        // response
        String sellerUsername = userClient.username(request.getSellerId());
        
        Set<ItemResponse> itemResponses = new HashSet<>();

        ItemResponse itemResponseApi = productClient.getProductByVariantId(request.getVariantId());
        itemResponseApi.setQuantity(request.getQuantity());
        itemResponseApi.setUpdatedAt(Instant.now());

        String latestFlashsaleId = "";
        try {
          latestFlashsaleId = peterClient.getLastestFlashSalesId();
        } catch (Exception e) {
          System.out.println("error: " + e.getMessage());
        }

        Long discount = 0L;
        try {
          discount = peterClient.getDiscountByFlashSaleIdAndProductId(latestFlashsaleId, itemResponseApi.getProductId());
        } catch (Exception e) {
          System.out.println("error: " + e.getMessage());
        }
        itemResponseApi.setDiscount(discount);

        itemResponses.add(itemResponseApi);
  
        SellerResponse sellerResponse = SellerResponse.builder()
        .sellerId(request.getSellerId())
        .sellerUsername(sellerUsername)
        .itemResponses(itemResponses)
        .updatedAt(Instant.now())
        .build();

        sellerResponses.add(sellerResponse);
        return sellerResponses;
        
      }
     
    
    }


    return sellerResponses;
  }
  

  @PreAuthorize("hasRole('ROLE_USER')")
  public Set<SellerResponse> deletee(String variantId) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    if (!userClient.doesUserExistByEmail(email)) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String sellerId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(sellerId)
      .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<SellerResponse> sellerResponses = new HashSet<>();
    Set<Seller> sellers = cart.getSellers();
    boolean itemDeleted = false;

    for (Iterator<Seller> sellerIterator = sellers.iterator(); sellerIterator.hasNext();) {
      Seller seller = sellerIterator.next();
      String sellerUsername = userClient.username(seller.getSellerId());
      Set<Item> items = seller.getItems();
      Set<ItemResponse> itemResponses = new HashSet<>();

      for (Iterator<Item> itemIterator = items.iterator(); itemIterator.hasNext();) {
        Item item = itemIterator.next();
        if (item.getVariantId().equals(variantId)) {
          itemIterator.remove(); 
          itemDeleted = true;
        } else {
          ItemResponse itemResponse = productClient.getProductByVariantId(item.getVariantId());
          itemResponse.setQuantity(item.getQuantity());
          itemResponse.setUpdatedAt(item.getUpdatedAt());
          itemResponses.add(itemResponse);
        }
      }

      if (items.isEmpty()) {
        sellerIterator.remove();
      } else {
          seller.setItems(items);
          SellerResponse sellerResponse = SellerResponse.builder()
            .sellerId(seller.getSellerId())
            .sellerUsername(sellerUsername)
            .itemResponses(itemResponses)
            .updatedAt(seller.getUpdatedAt())
            .build();
          sellerResponses.add(sellerResponse);
        }
    }

    if (itemDeleted) {
      cart.setSellers(sellers);
      cartRepository.save(cart);
    }

    return sellerResponses;
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse update(CartUpdateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(request.getVariantId() != null && !productClient.doesVariantExistById(request.getVariantId())){
      throw new AppException(ErrorCode.VARIANT_NOT_EXISTS);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<Seller> sellers = cart.getSellers();
    for(Seller seller : sellers){
      Set<Item> cartItems = seller.getItems();
      for(Item cartItem : cartItems){
        if(cartItem.getVariantId().equals(request.getVariantId())){
          cartItem.setQuantity(request.getQuantity());

          seller.setItems(cartItems);
          cart.setSellers(sellers);
          cartRepository.save(cart);

          return CartResponse.builder()
          .sellers(sellers)
          .build();
        }
      }
    }

    // Item cartItem = Item.builder()
    // .variantId(request.getVariantId())
    // .quantity(request.getQuantity())
    // .build();

    // return CartResponse.builder()
    // .sellers(sellers)
    // .build();

    throw new AppException(ErrorCode.CART_NOT_FOUND);
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse deleteItem(String variantId){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<Seller> sellers = cart.getSellers();
    OUTTERFOR:
    for(Seller seller : sellers){
      Set<Item> cartItems = seller.getItems();
      for(Item cartItem : cartItems){
        if(cartItem.getVariantId().equals(variantId)){
          cartItems.remove(cartItem);

          seller.setItems(cartItems);
          cart.setSellers(sellers);

          cartRepository.save(cart);

          break OUTTERFOR;
        }
      }
    }

    return CartResponse.builder()
    .sellers(sellers)
    .build();
    // throw new AppException(ErrorCode.CART_NOT_FOUND);
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  public CartResponse deleteSeller(String sellerId){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(!userClient.doesUserExistByEmail(email)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(!userClient.doesUserExistById(sellerId)){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String userId = userClient.userId(email);

    Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    Set<Seller> sellers = cart.getSellers();
    for(Seller seller : sellers){
      if(seller.getSellerId().equals(sellerId)){
        sellers.remove(seller);

        cart.setSellers(sellers);

        cartRepository.save(cart);

        return CartResponse.builder()
          .sellers(sellers)
          .build();
      }
    }


    throw new AppException(ErrorCode.CART_NOT_FOUND);
  }

 
  @PreAuthorize("hasRole('ROLE_USER')")
  public Set<SellerResponse> deleteSellerById(String sellerId){
    Cart cart = cartRepository.findByUserId(sellerId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    Set<SellerResponse> sellerResponses = new HashSet<>();
    Set<Seller> sellers = cart.getSellers();
    for(Seller seller : sellers) {
 
      if(seller.getSellerId().equals(sellerId)){
        sellers.remove(seller);
        cart.setSellers(sellers);

        cartRepository.save(cart);
      }
      else{
        String sellerUsername = userClient.username(sellerId);
        Set<ItemResponse> itemResponses = new HashSet<>();
        Set<Item> items = seller.getItems();

        for(Item item : items){
    
          ItemResponse itemResponse = productClient.getProductByVariantId(item.getVariantId());
          itemResponse.setQuantity(item.getQuantity());      
          itemResponse.setUpdatedAt(item.getUpdatedAt());
          itemResponses.add(itemResponse);
  
        }

        SellerResponse sellerResponse = SellerResponse.builder()
          .sellerId(sellerId)
          .sellerUsername(sellerUsername)
          .itemResponses(itemResponses)
          .updatedAt(seller.getUpdatedAt())
          .build();

          sellerResponses.add(sellerResponse);
      }
    }
  
    return sellerResponses;
    
  }
  
}
