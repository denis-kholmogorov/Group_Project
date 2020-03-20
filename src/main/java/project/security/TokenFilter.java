package project.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Создаем фильтр, который будет проверять наш токен из заголовка запроса.
 * В TokenConfig.class мы его добавим в SecurityFilters */

@Slf4j
public class TokenFilter extends GenericFilterBean
{

    private TokenProvider tokenProvider;

    @Autowired
    public TokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
       String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest); //Берем заголовок
       log.info("Получаем токен в фильтре");
       if(token != null && tokenProvider.validateToken(token)){ //проверяем его валидность
           log.info("Валидируем токен в фильтре");
           Authentication auth = tokenProvider.getAuthentication(token); //получаем аутентификацию

           if(auth != null){
               SecurityContextHolder.getContext().setAuthentication(auth); // добавляем в контекст security
               log.info("Ништяк токен в контексте");
           }
       }
       filterChain.doFilter(servletRequest, servletResponse); // переводим запрос дальше(разрешаем доступ)
    }
}
