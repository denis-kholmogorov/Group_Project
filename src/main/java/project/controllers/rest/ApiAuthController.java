package project.controllers.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.LoginRequestDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Person;
import project.security.TokenProvider;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * Данный контроллер служит для примера urls */


@Slf4j
@RestController
@RequestMapping("/api/v1/auth/")
public class ApiAuthController {

    private TokenProvider tokenProvider;

    private PersonService personService;

    @Autowired
    public ApiAuthController(TokenProvider tokenProvider, PersonService personService) {
        this.tokenProvider = tokenProvider;
        this.personService = personService;
    }


    @PostMapping(value = "login")
    ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) throws BadRequestException400 {
        ResponseDto responseDto = personService.login(loginRequestDto);
        if(responseDto == null) throw new BadRequestException400();
        return ResponseEntity.ok(responseDto);//необходимо оставить, обработать еще ошибки
    }


    @PostMapping(value = "logout")
    ResponseEntity logout(HttpServletRequest servletRequest) {
        try {
            Person person = tokenProvider.getPersonByRequest(servletRequest);
            person.setLastOnlineTime(new Date());
            personService.saveLastOnlineTime(person);
        }
        catch (Exception ex) {
            log.info("user was deleted");
        }
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

}
