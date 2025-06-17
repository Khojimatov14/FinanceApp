package financeapp.repository;

import java.util.Optional;
import financeapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// User (foydalanuvchi) entity uchun repository interfeysi
// JpaRepository orqali barcha CRUD amallarini bajarish mumkin bo'ladi
public interface UserRepository extends JpaRepository<User, Long> {

    // Email manzil orqali foydalanuvchini qidirish
    Optional<User> findByEmail(String email);
}
