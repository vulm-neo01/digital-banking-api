package com.neo.digital_banking_api.entity;

import com.neo.digital_banking_api.enums.TransactionStatus;
import com.neo.digital_banking_api.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transaction_from_account", columnList = "from_account_id"),
    @Index(name = "idx_transaction_to_account", columnList = "to_account_id"),
    @Index(name = "idx_transaction_status", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity {

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Amount format is invalid")
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "From account ID is required")
    @Column(name = "from_account_id", nullable = false)
    private Long fromAccountId;

    @Column(name = "to_account_id")
    private Long toAccountId; // Nullable for deposits/withdrawals

    @NotNull(message = "Transaction status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.NEW;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    // Business methods
    public boolean isDebit() {
        return type == TransactionType.WITHDRAWAL || type == TransactionType.TRANSFER;
    }

    public boolean isCredit() {
        return type == TransactionType.DEPOSIT || (type == TransactionType.TRANSFER && toAccountId != null);
    }

    public boolean isSuccessful() {
        return status == TransactionStatus.DONE;
    }

    public void markAsProcessed() {
        this.status = TransactionStatus.DONE;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsFailed(String reason) {
        this.status = TransactionStatus.FAILED;
        this.processedAt = LocalDateTime.now();
    }
}
