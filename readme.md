start: 
  1. start eureka server
  2. start apigateway
  3. start other services


1. trang seller update status order -> cập nhật trong order-detail

order cần thêm 2 field: recieveImage, shipperId

2. trang shipper update status order -> tạo trang dành riêng cho shipper: login, cập nhật order status
-> clone từ seller qua -> tạo folder shipper -> auth, order: shipper chỉ có 2 trang
shipper login chung userservice, cập nhật trạng thái chung với seller ở BackEnd
shipper được phép hủy đơn ???


3. sửa lại product detail/update -> không cho xóa variant

4. trang admin quản lý page: CRUD quản lý shipping, voucher shipping, peter voucher, petercategory, flashsale

5. trang user: flashsale -> tìm các flashsale với id khác -> tạo các flashsale bằng trang admin

6. trang FE: get all flashsale -> sort -> getlatest -> fetch to FE = hiện tại gắn id cứng