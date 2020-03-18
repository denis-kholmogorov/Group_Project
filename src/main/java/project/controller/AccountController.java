package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.RegistrationRequest;
import project.services.PersonService;

@RestController
@RequestMapping(path = "/account", produces="application/json")
@CrossOrigin(origins="*")
@Slf4j
public class AccountController {

    private PersonService personService;

    @Autowired
    public AccountController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request){

        personService.registerPerson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ok");
    }
}
