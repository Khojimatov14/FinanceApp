package financeapp.controller;

import java.util.List;
import java.util.Optional;
import financeapp.model.User;
import org.springframework.http.HttpStatus;
import financeapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Barcha foydalanuvchilarni ro‘yxatini qaytaradi
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Yangi foydalanuvchini yaratadi va bazaga saqlaydi
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A user with this email already exists.");
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Belgilangan ID bo‘yicha foydalanuvchini yangilaydi (ism, email, balans)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setBalance(userDetails.getBalance());
        return userRepository.save(user);
    }

    // ID bo‘yicha bitta foydalanuvchini qaytaradi
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
