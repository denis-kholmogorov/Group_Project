package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.AuthRequest;
import project.dto.ResponseDTO;
import project.models.Person;
import project.security.JwtTokenProvider;
import project.services.PersonService;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@Slf4j
@RequestMapping("/auth")
@CrossOrigin(origins="*", allowedHeaders = "*")
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

        Person data = personService.loginPerson(authRequest);
        String token = "";
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователя не существует");
        } else {
            token = jwtTokenProvider.createToken(data.getEmail());
            //res.addHeader(JwtTokenProvider.HEADER_STRING, token);
            data.setToken(token);
        }
        ResponseDTO<Person> responseDTO = new ResponseDTO<>("", new Date().getTime(), data);

        return ResponseEntity.ok(responseDTO);
    }
}
