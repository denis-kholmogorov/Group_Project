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

@Slf4j
@Component
public class TokenProvider
{
    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMillis;

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public TokenProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    protected void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String email){

        Claims claims = Jwts.claims().setSubject(email);
        log.info("Создан токен " + email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token){
        log.info("Получаем юзера " + token);
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        log.info("токен = " + bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer_")){
            log.info("Достаем токен из заголовка запроса " + bearerToken);
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
    try {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        if(claims.getBody().getExpiration().before(new Date())){
            log.info("Токен НЕ валиден");
            return false;
        }
        log.info("Токен валиден");
        return true;
    }catch (JwtException | IllegalArgumentException e){
        throw new JwtAuthentificationExecption("JWT token is expired or invalid");
    }


    }
    private List<String> getRolNames(List<Role> userRole){
        List<String> result = new ArrayList<>();
        userRole.forEach(role -> {
            result.add(role.getRoleName());
        });
        return result;
    }
}
