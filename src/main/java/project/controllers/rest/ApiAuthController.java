package project.controllers.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.LoginRequestDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDataObject;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.EmailAlreadyRegisteredException;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;


/**
 * Данный контроллер служит для примера urls */


@Slf4j
@RestController
@RequestMapping("/auth/")
public class ApiAuthController {


    private PersonService personService;

    @Autowired
    public ApiAuthController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(value = "login")
    ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto){
        ResponseDataObject responseDto = personService.login(loginRequestDto);
        if(responseDto == null) throw new EmailAlreadyRegisteredException();
        return ResponseEntity.ok(responseDto);//необходимо оставить
    }


    @PostMapping(value = "logout")
    ResponseEntity logout(HttpServletRequest request){
        Boolean result = personService.logout(request);
        return ResponseEntity.ok(new ResponseDataObject<>(new MessageResponseDto()));
    }




}
