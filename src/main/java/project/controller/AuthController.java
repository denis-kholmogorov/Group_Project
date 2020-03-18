package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.AuthRequest;
import project.dto.RegistrationRequest;
import project.models.Person;
import project.security.JwtTokenProvider;
import project.services.PersonService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/auth")
@CrossOrigin(origins="*")
public class AuthController {

    private PersonService personService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthController(PersonService personService, JwtTokenProvider jwtTokenProvider) {
        this.personService = personService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse res){

        log.info("trig");
        Person user = personService.loginPerson(authRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователя не существует");
        } else {
            String token = jwtTokenProvider.createToken(user.getEmail());
            res.addHeader(JwtTokenProvider.HEADER_STRING, JwtTokenProvider.TOKEN_PREFIX + token);
            log.info(token);
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/restore")
    public void restorePassword(){

    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello!";
    }
}
