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
        log.info("Запрос страницы");
       String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);
       if(token != null && tokenProvider.validateToken(token)){
           log.info("Прошли валидацю - ");
           Authentication auth = tokenProvider.getAuthentication(token);

           if(auth != null){
               SecurityContextHolder.getContext().setAuthentication(auth);
           }
       }
        log.info("Разрешен доступ");
        filterChain.doFilter(servletRequest, servletResponse);


    }
}
