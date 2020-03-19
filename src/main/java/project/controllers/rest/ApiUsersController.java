package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.services.PersonService;

@RestController
@RequestMapping(value = "/users/")
@AllArgsConstructor
public class ApiUsersController {
    private PersonService personService;


}
