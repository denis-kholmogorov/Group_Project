 package project.security;

 import lombok.extern.slf4j.Slf4j;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.web.filter.OncePerRequestFilter;

 import javax.servlet.FilterChain;
 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import java.io.IOException;
 import java.util.Collections;

 /**
  * В Spring Security каждый запрос проходит через цепочку фильтров.
  * Создадим новый фильтр, который будет проверять наличие jwt токена заголовке запроса.
  * Если все хорошо, аутентифицируемся и добавляем объект Authentication в security context
  * Для применения этого фильтра необходимо в классе SecurityConfig добавить этот класс в цепочку фильтров.
  * Отнаследуемся от класса OncePerRequestFilter - как следует из названия, этот фильтр срабатывает на запрос один раз,
  * что хорошо подходит для аутентификации.
  */

 @Slf4j
 public class JwtTokenVerifyFilter extends OncePerRequestFilter {

     private JwtTokenProvider jwtTokenProvider;

     public JwtTokenVerifyFilter(JwtTokenProvider jwtTokenProvider) {
         this.jwtTokenProvider = jwtTokenProvider;
     }

     @Override
     public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

         /*
          * Получаем токен из запроса, проверяем его валидность и, если все хорошо, аутентифицируемся
          * Метод filterChain.doFilter() отправляет запрос дальше по цепочке фильтров.
          */
         String token = jwtTokenProvider.resolveToken(request);
         if (token != null && !jwtTokenProvider.isExpired(token)){
             Authentication authentication = jwtTokenProvider.getAuthentication(token);
             if(authentication != null){
                 SecurityContextHolder.getContext().setAuthentication(authentication);
             }
         }
         filterChain.doFilter(request, response);
     }
 }
