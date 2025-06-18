package financeapp.security;

import java.io.IOException;
import financeapp.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import financeapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;



// Har bir HTTP so'rov uchun JWT tokenni tekshiradi va foydalanuvchini aniqlaydi.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;


    // Har bir so'rovda JWT tokenni ajratib, foydalanuvchini aniqlaydi.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        // HTTP sarlavhasidan "Authorization" ni oladi
        final String authHeader = request.getHeader("Authorization");

        // Token mavjudligini va "Bearer " bilan boshlanishini tekshiradi
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " so'zidan keyin token bo'ladi
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        // Agar foydalanuvchi emaili mavjud bo'lsa va hali autentifikatsiya qilinmagan bo'lsa
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(userEmail).orElse(null);

            // Agar token foydalanuvchiga mos bo'lsa, uni autentifikatsiya qiladi
            if (user != null && jwtService.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Security kontekstga o'rnatamiz
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Keyingi filterlarga o'tish
        filterChain.doFilter(request, response);
    }
}
