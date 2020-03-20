package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.repositories.PersonRepository;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {

    private PersonRepository personRepository;

    @Autowired
    public UserController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
