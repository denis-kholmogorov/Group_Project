package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.RegistrationRequest;
import project.models.Person;
import project.services.PersonService;
import project.util.EmailService;
import project.util.EmailServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping(path = "/account")
@CrossOrigin(origins="*")
@Slf4j
public class AccountController {

    private PersonService personService;

    @Autowired
    public AccountController(PersonService personService, EmailServiceImpl emailService) {
        this.personService = personService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request){

        personService.registerPerson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ok");
    }

    @PutMapping("/password/recovery")
    public void sendRecoveryEmail(@RequestParam("email") String email){

        personService.sendRecoveryPasswordEmail(email);
    }
}
