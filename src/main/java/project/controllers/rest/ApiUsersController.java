package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.responseDto.PersonDto;
import project.dto.responseDto.ResponseDataObject;
import project.handlerExceptions.UnauthorizationException;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/users/")
@AllArgsConstructor
public class ApiUsersController {


    private PersonService personService;

    @GetMapping("me")
    public ResponseEntity getMyPage(HttpServletRequest request){
        return ResponseEntity.ok(new ResponseDataObject<>(new PersonDto()));
    }
}
