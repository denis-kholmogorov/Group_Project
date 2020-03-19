package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.requestDto.RegistrationRequestDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.RegisterResponseDto;
import project.dto.responseDto.ResponseDataObject;
import project.services.PersonService;

@Slf4j
@RestController
@RequestMapping(value = "/account/")
@AllArgsConstructor
public class ApiAccountController {

    private PersonService personService;

   /* @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequestDto dto) {
        log.info("контроллер Register отработал");
        ResponseDataObject responseDto =  personService.registrationPerson(dto);

        if (responseDto != null) return ResponseEntity.ok(responseDto);

        else return ResponseEntity.status(400).body(null); //как обработать 400?
    }*/

}

