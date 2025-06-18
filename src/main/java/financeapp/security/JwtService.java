package financeapp.security;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.security.Key;
import io.jsonwebtoken.*;
import financeapp.model.User;
import java.util.function.Function;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


// Bu klass token yaratadi, tekshiradi va ichidagi ma’lumotlarni ajratib beradi.
@Service
public class JwtService {

    // application.properties dan o'qiladi
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;


    //Foydalanuvchi obyekti asosida token yaratadi.
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims) // qo'shimcha ma’lumotlar
                .setSubject(user.getEmail()) // token kimga tegishli
                .setIssuedAt(new Date()) // yaratilgan vaqti
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // tugash vaqti
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // imzolash algoritmi
                .compact(); // JWT tokenni stringga aylantiradi
    }


    //Token ichidagi foydalanuvchi emailini (subject) ajratib beradi.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    //Token yaroqliligini tekshiradi: email mos va token muddati tugamagan bo‘lishi kerak.
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }


    // Token muddati tugaganmi — shuni tekshiradi.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    // Tokenning tugash vaqtini olish.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // Token ichidagi ma’lumotlarni olish uchun generic metod.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    // Tokenni dekod qilib, barcha ma’lumotlarini olish.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // tokenni imzolovchi kalit
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    //Secret kalitdan tokenni imzolash uchun kalit yaratadi.
    private Key getSignInKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
