package project.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class TokenProvider
{
    @Autowired
    private UserDetailsService userDetailsService;

    /** Данная МАПА является хранилищем сессий и пользователей */
    private HashMap <String, String> store = new HashMap<>();

    public String createToken(HttpSession session, String email){
        String result = store.put(session.getId(), email);
        log.info("Добавлен токен " + email);
        return result;
    }

    public Authentication getAuthentication(HttpSession session){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmail(session));
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }

    public String getUserEmail(HttpSession session){

        String email = store.get(session.getId());
        log.info("Получаем юзера " +  email);
        return email;
    }

    public boolean validitySession(HttpSession session){
        if(store.containsKey(session.getId())) {
            log.info("Токен валиден");
            return true;
        }
        return false;
    }

    public boolean tokenDelete(HttpSession session){
        log.info("Сессия найдена " + store.containsKey(session.getId()) + " кол-во ключей " + store.size());
        log.info(session.getId().equals(store.get(0)) + " " + store.get(session.getId()));
        store.keySet().forEach(key->{
            log.info(key);
        });
        if(store.containsKey(session.getId())) {

            store.remove(session.getId());
            return true;
        }
        return false;

    }

    private List<String> getRolNames(List<Role> userRole){
        List<String> result = new ArrayList<>();
        userRole.forEach(role -> {
            result.add(role.getRoleName());
        });
        return result;
    }
}
