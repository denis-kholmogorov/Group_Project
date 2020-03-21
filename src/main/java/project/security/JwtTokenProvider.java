package project.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import project.models.Person;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Класс для создания и работы с jwt-токеном
 * jwt-токен всегда начинается с префикса "Bearer " и устанавливается в хэдер "Authorization".
 * Таким образом нам понадобится фильтр, который будет проверять наличие такого токена в хэдере.
 * Этот фильтр мы реализуем в классе JwtTokenVerifyFilter.
 */

@Component
@Slf4j
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    private PersonDetailsService personDetailsService;

    @Autowired
    public JwtTokenProvider(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    /**
     * Метод, в котором происходит вся работа по созданию токена при помощи библиотеки auth0
     */
    public String createToken(String email) {

        Date expirationDate = new Date(new Date().getTime() + validityInMilliseconds);
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    Authentication getAuthentication(String token) {
        Person person = (Person) personDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(person.getEmail(), person.getPassword(), person.getAuthorities());
    }

    /**
     * Метод получения токена из хэдера запроса
     */
    String resolveToken(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    boolean isExpired(String token) {

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));
        Date expirationDate = decodedJWT.getExpiresAt();
        return new Date().after(expirationDate);
    }

    private String getEmail(String token) {
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject().trim();
    }
}