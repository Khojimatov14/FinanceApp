package financeapp.model;

import lombok.*;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id // Har bir foydalanuvchi uchun unikal ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // Foydalanuvchi ismi

    @Column(nullable = false, unique = true) // Email bo'sh bo'lmasligi va unikal bo'lishi belgilaydi
    private String email; // Foydalanuvchining emaili

    @Column(nullable = false) // Balans maydoni bo'sh bo'lishi mumkin emas
    private BigDecimal balance; // Foydalanuvchining hisob balansi (pul miqdori)

    @Column(nullable = false) // Parol maydoni bo'sh bo'lishi mumkin emas
    private String password; // Foydalanuvchi paroli
}
