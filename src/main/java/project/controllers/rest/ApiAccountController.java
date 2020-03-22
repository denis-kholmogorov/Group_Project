package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.RegistrationRequestDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.EmailAlreadyRegisteredException;
import project.services.PersonService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/account/")
@AllArgsConstructor
public class ApiAccountController {
    private PersonService personService;

    @PostMapping(value = "register")
    public ResponseEntity<ResponseDto<MessageResponseDto>> register(@RequestBody RegistrationRequestDto dto) throws EmailAlreadyRegisteredException {
        log.info("контроллер Register отработал");
        personService.registrationPerson(dto);
        return ResponseEntity.ok(new ResponseDto(new MessageResponseDto()));
    }

    @PutMapping(value = "password/recovery")
    public ResponseEntity<ResponseDto<MessageResponseDto>> sendRecoveryEmail(@RequestBody Map<String, String> email){
        return ResponseEntity.ok(personService.sendRecoveryPasswordEmail(email.get("email")));
    }

}

