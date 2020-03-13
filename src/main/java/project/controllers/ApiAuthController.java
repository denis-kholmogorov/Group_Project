package project.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import project.dto.LoginRequestDto;
import project.dto.RegistrationRequestDto;
import project.models.Person;
import project.security.JwtAuthentificationExecption;
import project.security.TokenProvider;
import project.services.PersonService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private AuthenticationManager authenticationManager;

    private TokenProvider tokenProvider;

    private PersonService personService;


    public ApiAuthController(AuthenticationManager authenticationManager, TokenProvider tokenProvider, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.personService = personService;
    }

    @PostMapping(value = "/registration")
    ResponseEntity registrations(@RequestBody RegistrationRequestDto dto){
        log.info(dto.getEmail() + "  " + dto.getPassword());
        personService.registrationPerson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping(value = "/login")
    ResponseEntity login(@RequestBody LoginRequestDto dto){
       // log.info(headers.getHeaders().get("Authorization") + "");
        log.info("Логинится Юзер" + dto.getEmail() + "  " + dto.getPassword());
        try {
            String email = dto.getEmail();
            String password = dto.getPassword();
            // authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            Person person = personService.findPersonByEmail(email);
            if (person != null) {
                log.info("Юзер НЕ найден");
            }
            log.info("Юзер найден, генерируется токен");
            String token = tokenProvider.createToken(email);
            Map<Object, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("token", token);
            return ResponseEntity.ok(response);
        }catch (JwtAuthentificationExecption e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен не валиден");
        }
    }

    @GetMapping(value = "/")
    ResponseEntity logout(HttpSession session){
        /*log.info("Юзер выходит " + session.getId());
        if(personService.logout(session)) return ResponseEntity.ok().body("Токен удален");*/
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не сработало");

    }

}
