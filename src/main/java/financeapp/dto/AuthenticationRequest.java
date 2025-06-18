package financeapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    private String email;     // Foydalanuvchining email manzili
    private String password;  // Parol
}
