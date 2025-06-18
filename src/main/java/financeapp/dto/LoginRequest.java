package financeapp.dto;

import lombok.Data;

// Login endpoint uchun email va password so‘rov modeli
@Data
public class LoginRequest {
    private String email;
    private String password;
}
