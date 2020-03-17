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
@RequestMapping("/api/v1/auth")
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

        Map<String, Object> response = new HashMap<>();
        Person person = personService.loginPerson(authRequest);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователя не существует");
        } else {
            String token = jwtTokenProvider.createToken(person.getEmail());
            res.addHeader(JwtTokenProvider.HEADER_STRING, JwtTokenProvider.TOKEN_PREFIX + token);
            response.put(token, person);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request){

        personService.registerPerson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ok");
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
