package com.neo.digital_banking_api.enums;

/**
 * Transaction status lifecycle
 */
public enum TransactionStatus {
    NEW,        // Giao dịch mới được tạo
    PROCESSING, // Đang xử lý giao dịch
    DONE,       // Giao dịch hoàn thành thành công
    FAILED      // Giao dịch thất bại
}
