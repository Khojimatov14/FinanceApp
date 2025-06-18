package financeapp.security;

import financeapp.model.User;
import lombok.RequiredArgsConstructor;
import financeapp.dto.AuthenticationRequest;
import financeapp.repository.UserRepository;
import financeapp.dto.AuthenticationResponse;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;          // Foydalanuvchilar bazasini boshqaradi
    private final PasswordEncoder passwordEncoder;        // Parollarni xavfsiz saqlash uchun
    private final JwtService jwtService;                  // JWT token yaratish uchun
    private final AuthenticationManager authenticationManager; // Loginni tekshirish uchun

    // Ro'yxatdan o'tkazish (Register)
    public AuthenticationResponse register(User request) {
        // Email bo'yicha foydalanuvchi mavjud emasligini tekshiradi
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu email allaqachon ro'yxatdan o'tgan");
        }

        // Parolni kodlab saqlaydi
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        // Foydalanuvchini saqlaydi
        User savedUser = userRepository.save(request);

        // JWT token yaratadi
        String jwtToken = jwtService.generateToken(savedUser);

        // Tokenni javob sifatida qaytaradi
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // Tizimga kirish (Login)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Email va parolni tekshiradi
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Email orqali foydalanuvchini yuklaydi
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Foydalanuvchi topilmadi"));

        // JWT token yaratadi
        String jwtToken = jwtService.generateToken(user);

        // Tokenni javob sifatida qaytaradi
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
