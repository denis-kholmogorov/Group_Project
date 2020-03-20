package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.requestDto.LoginRequestDto;
import project.dto.requestDto.RegistrationRequestDto;
import project.dto.responseDto.PersonDtoWithToken;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDataObject;
import project.handlerExceptions.EmailAlreadyRegisteredException;
import project.models.Person;
import project.models.Role;
import project.models.Token;
import project.repositories.PersonRepository;
import project.repositories.TokenRepository;
import project.security.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

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
    TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseDataObject<MessageResponseDto> registrationPerson(RegistrationRequestDto dto){
        Person exist = personRepository.findPersonByEmail(dto.getEmail()).orElse(null);
        if(exist != null) throw new EmailAlreadyRegisteredException();
        Person person = new Person();
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        person.setEmail(dto.getEmail());
        person.setPassword(encoder.encode(dto.getPasswd1()));
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setRegDate(new Date());
        person.setRoles(Collections.singleton(role));

        personRepository.save(person);
        return (new ResponseDataObject(new MessageResponseDto()));

    }


    public ResponseDataObject login(LoginRequestDto dto){
        String email = dto.getEmail();
        String password = dto.getPassword();
        Person person = personRepository.findPersonByEmail(email).orElse(null);//необходимо оставить
        if (person == null) {
            //
        }
        Token jwtToken = new Token();
        String token = tokenProvider.createToken(email);//необходимо оставить
        jwtToken.setToken(token);
        jwtToken.setDateCreated(Calendar.getInstance());
        jwtToken.setPerson(person);
        Token t = tokenRepository.save(jwtToken);
        log.info(t.getPerson().getEmail());

        PersonDtoWithToken personDto = new PersonDtoWithToken();
        personDto.setId(person.getId());
        personDto.setFirst_name(person.getFirstName());
        personDto.setLast_name(person.getLastName());
        personDto.setEmail(person.getEmail());
        personDto.setPhone(person.getPhone());
        personDto.setPhoto(person.getPhoto());
        personDto.setAbout(person.getAbout());
        personDto.set_blocked(person.isBlocked());
        personDto.setToken(token);
        return new ResponseDataObject(personDto);
    }


    public Person findPersonByEmail(String email){
        Person person = personRepository.findPersonByEmail(email).orElse(null);

        if(person == null){return null; }
        return person;
    }

    public boolean logout(HttpServletRequest request){
        String token = tokenProvider.resolveToken(request);
        tokenRepository.deleteByToken(token);
        return true;
    }



}
