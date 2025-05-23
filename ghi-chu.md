start: 
  1. start eureka server
  2. start apigateway
  3. start other services

service:
  1. userservice: 
    entity: user, role, permission, address
  2. productservice: 
    entity: category, product, variant, info
  3. cartservice:
    entity: cart
      cart: id, userId, variantId, quantity
  4. orderservice:
    entity: order, payment
      order: id, addressId, variantId, quantity, price, status, paymentId, 
      payment: id, orderId, status,
  5. fileservice
