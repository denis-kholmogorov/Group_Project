package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.models.Person;
import project.models.util.entity.PersonFactory;
import project.models.util.entity.ResponseDataObject;

//контроллер для тестирования
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    ResponseEntity<?> me() throws Exception {
        PersonFactory personFactory = new PersonFactory();
        ResponseDataObject<Person> response = new ResponseDataObject<>();
        response.setData(personFactory.getPerson());
        return ResponseEntity.ok(response);
    }
}
