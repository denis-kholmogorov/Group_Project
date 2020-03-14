package project.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
/**
 * Данный класс работает с нашим токеном*/
@Slf4j
@Component
public class TokenProvider
{
    @Value("${jwt.token.secret}")
    private String secret; // секретное слово из application.yml

    @Value("${jwt.token.expired}")
    private long validityInMillis; // время действия из application.yml

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public TokenProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes()); // шифрование токена перед запуском класса
    }

    public String createToken(String email){ //создание токена

        Claims claims = Jwts.claims().setSubject(email); //создаем клайм
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);
        return Jwts.builder()       // создаем токен
                .setClaims(claims)  // установка клайм
                .setIssuedAt(now)   // установка даты создания
                .setExpiration(validity) // установка действия создания
                .signWith(SignatureAlgorithm.HS256, secret) //хэширование секретного кода
                .compact();
    }

    /** Получение аутентификации по токену*/
    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /** Получение email по токену*/
    public String getUserEmail(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    /** Получение токена из header запроса*/
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer_")){
            return bearerToken.substring(7);
        }
        return null;
    }

    /** Валидация токена*/
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthentificationExecption("JWT token is expired or invalid");
        }
    }

}
