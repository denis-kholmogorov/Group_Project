package project.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dto.LoginRequestDto;
import project.dto.RegistrationRequestDto;
import project.services.PersonService;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    PersonService personService;

    @PostMapping(value = "/registration")
    ResponseEntity registrations(@RequestBody RegistrationRequestDto dto){

        log.info(dto.getEmail() + "  " + dto.getPassword());
        personService.registrationPerson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping(value = "/login")
    ResponseEntity login(@RequestBody LoginRequestDto dto, HttpSession session){
        log.info("Логинится Юзер" + dto.getEmail() + "  " + dto.getPassword());
        if(personService.login(dto, session)) return ResponseEntity.ok().body("Получил доступ");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не сработало");

    }

    @GetMapping(value = "/logout")
    ResponseEntity logout(HttpSession session){
        log.info("Юзер выходит " + session.getId());
        if(personService.logout(session)) return ResponseEntity.ok().body("Токен удален");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не сработало");

    }

}
