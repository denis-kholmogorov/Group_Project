package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.PasswordSetDto;
import project.dto.requestDto.RegistrationRequestDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/account/")
@AllArgsConstructor
public class ApiAccountController {
    private PersonService personService;

    @PostMapping(value = "register")
    public ResponseEntity<ResponseDto<MessageResponseDto>> register(@RequestBody RegistrationRequestDto dto) throws BadRequestException400 {
        log.info("контроллер Register отработал");
        personService.registrationPerson(dto);
        return ResponseEntity.ok(new ResponseDto(new MessageResponseDto()));
    }

    @PutMapping(value = "password/recovery")
    public ResponseEntity<ResponseDto<MessageResponseDto>> sendRecoveryEmail(@RequestBody Map<String, String> email) throws BadRequestException400 {
        return ResponseEntity.ok(personService.sendRecoveryPasswordEmail(email.get("email")));
    }

    @PutMapping("password/set")
    public ResponseEntity<ResponseDto<MessageResponseDto>> setNewPassword(@RequestBody PasswordSetDto passwordSetDto, HttpServletRequest request) throws BadRequestException400 {
        return ResponseEntity.ok(personService.setNewPassword(passwordSetDto, request));
    }
}

