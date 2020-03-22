package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.ResponseDTO;
import project.models.Person;
import project.repositories.PersonRepository;

import java.util.Date;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private PersonRepository personRepository;

    @Autowired
    public UserController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/me")
    public ResponseDTO getUser() {

        Person person =  personRepository.findByEmail("vLenin@yandex.ru").get();
        return new ResponseDTO("", new Date().getTime(), person);
    }
}
