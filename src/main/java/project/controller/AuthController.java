package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.models.User;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    ResponseEntity<?> newUser(@RequestBody User newUser) {
        System.out.println(newUser.getEmail() + " " + newUser.getPassword());
        return ResponseEntity.ok("");
    }
}
