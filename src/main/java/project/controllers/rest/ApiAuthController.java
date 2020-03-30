package project.controllers.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.LoginRequestDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.handlerExceptions.UnauthorizationException401;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;


/**
 * Данный контроллер служит для примера urls */


@Slf4j
@RestController
@RequestMapping("/api/v1/auth/")
public class ApiAuthController {


    private PersonService personService;

    @Autowired
    public ApiAuthController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(value = "login")
    ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) throws BadRequestException400 {
        ResponseDto responseDto = personService.login(loginRequestDto);
        if(responseDto == null) throw new BadRequestException400();//изменить ошибку
        return ResponseEntity.ok(responseDto);//необходимо оставить, обработать еще ошибки
    }

    @PostMapping(value = "logout")
    ResponseEntity logout(HttpServletRequest request) throws BadRequestException400 {
        Boolean result = personService.logout(request);
        return ResponseEntity.ok(new ResponseDto(new MessageResponseDto()));//обработать еще ошибки
    }

    @GetMapping("error")
    ResponseEntity error401(){
        throw new UnauthorizationException401();
    }

}
