package com.tranxuanphong.orderservice.enums;


public enum OrderStatus {
    PENDING,
    SELLER_CANCELLED,
    SELLER_CHECKING,
    SELLER_PREPAIRING,
    SELLER_PREPAIRED,
    SHIPPER_TAKING,
    SHIPPER_TAKEN,
    DISPATCHED,
    DELIVERING,
    DELIVERD,
    RETURN,
    REFUND,
    CANCELLED
}
