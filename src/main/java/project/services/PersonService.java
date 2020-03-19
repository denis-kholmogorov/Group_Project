package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.requestDto.LoginRequestDto;
import project.dto.responseDto.*;
import project.dto.requestDto.RegistrationRequestDto;
import project.models.Person;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseDataObject<MessageResponseDto> registrationPerson(RegistrationRequestDto dto){
        Optional<Person> optional = personRepository.findPersonByEmail(dto.getEmail());
        if (optional.isPresent()) return null;

        Person personBuilder = Person.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPasswd1()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .regDate(LocalDateTime.now())
                .build();

        Person personSaved = personRepository.save(personBuilder);


        log.info(encoder.matches(dto.getPasswd1(), personSaved.getPassword()) + " password одинаковый");
        log.info("Пользователь с данным " + personSaved.getEmail());
        ResponseDataObject<MessageResponseDto> responseDto = new ResponseDataObject<>();

        responseDto.setData(new MessageResponseDto());
        return responseDto;

    }


    public LoginResponseDto login(LoginRequestDto dto){
        String email = dto.getEmail();
        String password = dto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)); //необходимо оставить
        Person person = personRepository.findPersonByEmail(email).orElse(null);//необходимо оставить
        if (person == null) {
            return null;
        }
        String token = tokenProvider.createToken(email);//необходимо оставить
        return new LoginResponseDto(email, token);
    }

    public Person findPersonByEmail(String email){
        Person person = personRepository.findPersonByEmail(email).orElse(null);

        if(person == null){return null; }
        return person;
    }


}
