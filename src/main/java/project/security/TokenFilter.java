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
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;
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
        HttpServletRequest servletRequest1 = (HttpServletRequest) servletRequest;
        if(tokenProvider.validitySession(servletRequest1.getSession())){
            Authentication authentication = tokenProvider.getAuthentication(servletRequest1.getSession());

            if(authentication != null){
                log.info("Авторизация прошла");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
