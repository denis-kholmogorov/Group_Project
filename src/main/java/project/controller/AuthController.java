package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.models.User;
import project.models.util.entity.PersonFactory;
import project.models.util.entity.ResponseDataMap;
import project.models.util.entity.Token;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    ResponseEntity<?> newUser(@RequestBody User newUser) throws Exception {
        PersonFactory personFactory = new PersonFactory();
        ResponseDataMap response = new ResponseDataMap();
        response.put(personFactory.getPerson());
        Token token = new Token();
        token.setToken("123456");
        response.put(token);
        return ResponseEntity.ok(response);
    }
}
