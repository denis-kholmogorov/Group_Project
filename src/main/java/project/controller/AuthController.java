package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.AuthRequest;
import project.dto.RegistrationRequest;
import project.dto.ResponseDTO;
import project.models.Person;
import project.repositories.PersonRepository;
import project.security.JwtTokenProvider;
import project.services.PersonService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/auth")
@CrossOrigin(origins="*")
public class AuthController {

    private PersonService personService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
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
            res.addHeader(JwtTokenProvider.HEADER_STRING, token);
            log.info(token);
        }
        ResponseDTO<Person> responseDTO = new ResponseDTO<>("", new Timestamp(new Date().getTime()), user);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello!";
    }
}
