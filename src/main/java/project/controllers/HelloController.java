package project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hello/")
public class HelloController {

    @GetMapping(value = "/user")
    public ResponseEntity helloPeople(){
        return ResponseEntity.ok().body("Юзер Работает!!!!");
    }

    @GetMapping(value = "/admin")
    public ResponseEntity helloAdmin(){
        return ResponseEntity.ok().body("Админ Работает!!!!");
    }
}
