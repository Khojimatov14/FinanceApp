package financeapp.model;

import lombok.*;
import java.math.BigDecimal;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id // Har bir tranzaksiya uchun unikal ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Avtomatik inkrement qilinadi
    private Long id;
    private BigDecimal amount; // Tranzaksiya summasi
    private String description; // Tranzaksiya uchun izohi
    private LocalDateTime timestamp = LocalDateTime.now(); // Tranzaksiya yaratilgan vaqti (default: hozirgi vaqt)

    @ManyToOne // Ko‘p tranzaksiyalar bitta foydalanuvchiga tegishli bo‘lishi mumkin
    @JoinColumn(name = "user_id") // transactions jadvalidagi user_id ustun orqali user jadvali bilan bog‘lanadi
    private User user;
}

