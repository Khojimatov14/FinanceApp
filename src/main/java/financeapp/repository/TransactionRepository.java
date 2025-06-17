package financeapp.repository;

import java.util.List;
import financeapp.model.User;
import java.time.LocalDateTime;
import financeapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

// Transaction (tranzaksiya) entity uchun repository interfeysi
// JpaRepository orqali CRUD amallarni avtomatik bajarish mumkin bo'ladi
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Ma'lum bir foydalanuvchiga tegishli barcha tranzaksiyalarni olish
    List<Transaction> findByUser(User user);

    // Ko'rsatilgan sana oralig'idagi barcha tranzaksiyalarni olish
    List<Transaction> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
}
