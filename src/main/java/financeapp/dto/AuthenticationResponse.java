package financeapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token; // Tizimga muvaffaqiyatli kirganda qaytariladigan JWT token
}
