# Giải thích các Annotation JPA và BaseEntity

## 1. @MappedSuperclass

### Chức năng:
- **Không tạo bảng riêng**: Class được đánh dấu `@MappedSuperclass` sẽ **không** tạo bảng riêng trong database
- **Chia sẻ thuộc tính**: Các thuộc tính của class này sẽ được "kế thừa" bởi các entity con
- **Tái sử dụng code**: Tập trung các trường chung vào một nơi

### Ví dụ:
```java
@MappedSuperclass
public abstract class BaseEntity {
    private Long id;
    private LocalDateTime createdAt;
    // Các trường này sẽ xuất hiện trong bảng của entity con
}

@Entity
public class User extends BaseEntity {
    private String name;
    // Bảng User sẽ có: id, createdAt, name
}
```

### Khi nào dùng:
- Khi muốn chia sẻ các trường chung mà **không muốn** tạo bảng riêng
- Các trường audit, ID, timestamps
- Các trường metadata

---

## 2. @EntityListeners

### Chức năng:
- **Đăng ký Event Listener**: Cho phép class entity "lắng nghe" các sự kiện JPA
- **Tự động thực thi code**: Khi có thao tác CRUD, listener sẽ được gọi
- **Audit tự động**: Thường dùng cho audit trail

### Ví dụ:
```java
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    // AuditingEntityListener sẽ tự động set createdAt, updatedAt, createdBy, updatedBy
}
```

### Các Listener phổ biến:
- `AuditingEntityListener`: Cho audit tự động
- `CustomEntityListener`: Listener tùy chỉnh

---

## 3. @Version

### Chức năng:
- **Optimistic Locking**: Ngăn chặn concurrent modification
- **Phiên bản hóa**: Mỗi lần update, version tăng lên
- **Xử lý xung đột**: Nếu version không khớp, throw exception

### Cách hoạt động:
```java
@Version
private Long version = 0L;

// Khi update:
UPDATE table SET ..., version = version + 1 WHERE id = ? AND version = ?
```

### Ví dụ thực tế:
```java
// User A đọc: version = 5
// User B đọc: version = 5
// User A update: version = 6 (thành công)
// User B update: version = 5 (thất bại - OptimisticLockException)
```

---

## 4. AuditingEntityListener trong AppConfig

### Chức năng:
- **Audit tự động**: Tự động set các trường audit khi save/update
- **Không cần code thủ công**: Không cần `entity.setCreatedAt(now())`
- **Tích hợp Spring Security**: Có thể lấy thông tin user hiện tại

### Cấu hình trong AppConfig:
```java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Lấy thông tin user từ SecurityContext
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return Optional.of(auth.getName()); // Trả về username
        };
    }
}
```

### Cách hoạt động:
1. Khi `entity.save()` → `AuditingEntityListener` được gọi
2. Tự động set `createdAt`, `updatedAt`
3. Gọi `auditorProvider` để lấy `createdBy`, `updatedBy`

---

## 5. Có phải tất cả class nên extend BaseEntity?

### **Câu trả lời: KHÔNG PHẢI TẤT CẢ, nhưng HẦU HẾT nên extend**

### ✅ Nên extend BaseEntity:
- **Domain Entities**: User, Account, Transaction, Customer
- **Business Entities**: Các entity chính trong hệ thống banking
- **Auditable Entities**: Cần track ai tạo/sửa/xóa

### ❌ Không cần extend BaseEntity:
- **Lookup/Reference Tables**: Country, Currency, Status (thường không đổi)
- **Configuration Tables**: AppConfig, SystemSettings
- **Temporary/Cache Entities**: Session data, cache
- **Junction Tables**: Many-to-many relationship tables
- **Read-only Entities**: Views, reports

### Ví dụ trong Banking API:

```java
// ✅ Nên extend BaseEntity
@Entity
public class Account extends BaseEntity {
    private String accountNumber;
    private BigDecimal balance;
    // Kế thừa: id, timestamps, audit, version, soft delete
}

@Entity
public class Transaction extends BaseEntity {
    private String transactionId;
    private BigDecimal amount;
    // Kế thừa tất cả audit fields
}

// ❌ Không cần extend BaseEntity
@Entity
public class Currency extends BaseEntity { // Có thể không cần
    private String code; // USD, VND
    private String name;
    // Currency ít khi thay đổi, có thể không cần audit chi tiết
}

@Entity
public class Country { // Không extend
    @Id private String code; // VN, US
    private String name;
    // Reference data, không cần audit
}
```

### Lý do cho Banking System:

#### 🏦 **Regulatory Compliance**:
- **Audit Trail**: Ngân hàng phải track mọi thay đổi
- **Data Retention**: Lưu lịch sử giao dịch
- **Accountability**: Biết ai làm gì, khi nào

#### 🔒 **Security & Integrity**:
- **Version Control**: Ngăn concurrent updates
- **Soft Delete**: Không mất dữ liệu nhạy cảm
- **Audit Fields**: Track ai tạo/sửa tài khoản

#### 📊 **Business Requirements**:
- **Historical Data**: Lưu lịch sử thay đổi số dư
- **Compliance Reports**: Báo cáo audit cho regulator
- **Fraud Detection**: Track suspicious activities

---

## 6. Best Practices cho BaseEntity

### ✅ Đúng:
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id @GeneratedValue private Long id;
    @Version private Long version;
    @CreatedDate private LocalDateTime createdAt;
    @LastModifiedDate private LocalDateTime updatedAt;
    @CreatedBy private String createdBy;
    @LastModifiedBy private String updatedBy;
    private Boolean isDeleted = false;
}
```

### ❌ Tránh:
- Không để các trường business logic trong BaseEntity
- Không override audit fields trong entity con
- Không quên `@EnableJpaAuditing`

---

## 7. Kết luận

**BaseEntity là foundation** của hệ thống banking API vì:

1. **Compliance**: Đáp ứng yêu cầu pháp lý về audit
2. **Consistency**: Tất cả entities có cấu trúc giống nhau
3. **Maintainability**: Dễ thay đổi logic chung
4. **Security**: Ngăn concurrent modifications
5. **Data Integrity**: Soft delete bảo vệ dữ liệu

**Không phải tất cả class đều cần extend**, nhưng **hầu hết domain entities nên extend** để có audit trail đầy đủ cho banking system! 🏦✨
