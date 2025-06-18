package financeapp.controller;

import java.util.List;
import java.math.BigDecimal;
import financeapp.model.User;
import java.time.LocalDateTime;
import financeapp.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import financeapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import financeapp.repository.TransactionRepository;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    // TransactionRepository va UserRepository ni Spring orqali bog'lash
    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    // Barcha tranzaksiyalar ro'yxatini qaytaradi
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Foydalanuvchiga tegishli tranzaksiyalar ro'yxatini qaytaradi
    @GetMapping("/user/{userId}")
    public List<Transaction> getByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return transactionRepository.findByUser(user);
    }

    // Berilgan sanalar oralig'ida amalga oshirilgan tranzaksiyalarni qaytaradi
    @GetMapping("/date")
    public List<Transaction> getByDate(@RequestParam String from,
                                       @RequestParam String to) {
        LocalDateTime fromDate = LocalDateTime.parse(from + "T00:00:00"); // Boshlanish sanasi
        LocalDateTime toDate = LocalDateTime.parse(to + "T23:59:59");     // Tugash sanasi
        return transactionRepository.findByTimestampBetween(fromDate, toDate);
    }

    // Foydalanuvchi balansiga qo'shish yoki kamaytirish uchun tranzaksiya yaratadi
    @PostMapping
    public Transaction createTransaction(@RequestParam Long userId,
                                         @RequestParam Double amount,
                                         @RequestParam(required = false) String description) {
        User user = userRepository.findById(userId).orElseThrow();

        Transaction tx = new Transaction();
        tx.setAmount(java.math.BigDecimal.valueOf(amount));
        tx.setDescription(description);
        tx.setUser(user);

        // Balansni yangilaydi (ijobiy bo'lsa qo'shiladi, manfiy bo'lsa ayiriladi)
        user.setBalance(user.getBalance().add(java.math.BigDecimal.valueOf(amount)));
        userRepository.save(user);

        return transactionRepository.save(tx);
    }

    // Pulni bir foydalanuvchidan boshqasiga o'tkazadi
    @PostMapping("/transfer")
    public ResponseEntity<String> transferBalance(@RequestParam Long fromUserId,
                                                  @RequestParam Long toUserId,
                                                  @RequestParam BigDecimal amount,
                                                  @RequestParam String description) {

        // Miqdor musbat bo'lishi shart
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");
        }

        // Jo'natuvchi topish
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));

        // Qabul qiluvchini topish
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        // Jo'natuvchida yetarli mablag' borligini tekshiradi
        if (fromUser.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        // Pulni jo'natuvchi hisobidan ayirish
        fromUser.setBalance(fromUser.getBalance().subtract(amount));
        userRepository.save(fromUser);

        // Pulni qabul qiluvchi hisobiga qo'shish
        toUser.setBalance(toUser.getBalance().add(amount));
        userRepository.save(toUser);

        // Jo'natuvchi uchun chiqim tranzaksiyasini yaratish
        Transaction outgoing = Transaction.builder()
                .amount(amount.negate()) // Manfiy miqdor
                .description(description)
                .timestamp(LocalDateTime.now())
                .user(fromUser)
                .build();

        // Qabul qiluvchi uchun kirim tranzaksiyasini yaratish
        Transaction incoming = Transaction.builder()
                .amount(amount) // Musbat miqdor
                .description("Received from user " + fromUser.getName())
                .timestamp(LocalDateTime.now())
                .user(toUser)
                .build();

        // Ikkala tranzaksiyani saqlash
        transactionRepository.save(outgoing);
        transactionRepository.save(incoming);

        return ResponseEntity.ok("Transfer successful");
    }

}
