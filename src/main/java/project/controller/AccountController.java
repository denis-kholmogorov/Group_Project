package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.models.Person;

//контроллер для тестирования
@Controller
@RequestMapping("/api/v1/account")
public class AccountController {

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody Person newPerson) {
        System.out.println(newPerson);
        return ResponseEntity.ok().build();
    }
}
