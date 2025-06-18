package financeapp.controller;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import financeapp.dto.AuthenticationResponse;
import financeapp.model.User;
import financeapp.dto.LoginRequest;
import financeapp.dto.RegisterRequest;
import financeapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import financeapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // Foydalanuvchini ro'yxatdan o'tkazadi
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Bu email bilan foydalanuvchi allaqachon mavjud");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // parolni shifrlab saqlaymiz
                .balance(BigDecimal.ZERO)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("status: 200\n message: Siz muvaffaqiyatli ro'yxatdan o'tdingiz");
    }


    // Foydalanuvchini login qiladi va JWT token qaytaradi
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        // Email mavjudligini tekshiradi
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthenticationResponse(401, "Email yoki parol noto'g'ri", null));
        }

        User user = userOptional.get();

        // Parol to'g'riligini tekshiradi
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthenticationResponse(401, "Email yoki parol noto'g'ri", null));
        }

        // Agar email va parol to'g'ri bo‘lsa, JWT token yaratamiz
        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthenticationResponse(200, "Login muvaffaqiyatli amalga oshdi", jwtToken));
    }

    // Token bo'yicha foydalanuvchi malumotlarini qaytaradi
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Foydalanuvchi aniqlanmadi");
        }

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }


    // Barcha foydalanuvchilarni ro‘yxatini qaytaradi
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    // Belgilangan ID bo‘yicha foydalanuvchini yangilaydi
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setBalance(userDetails.getBalance());
        return userRepository.save(user);
    }
}
