package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.requestDto.LoginRequestDto;
import project.dto.requestDto.RegistrationRequestDto;
import project.dto.responseDto.PersonDtoWithToken;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.EmailAlreadyRegisteredException;
import project.models.Person;
import project.models.Role;
import project.models.Token;
import project.repositories.PersonRepository;
import project.repositories.TokenRepository;
import project.security.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
    TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    //    @PostConstruct
//    public void init() {
//        Person person = new Person();
//        person.setFirstName("Ilya");
//        person.setEmail("il@mail.ru");
//
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String password = passwordEncoder.encode("qweasdzxc");
//        person.setPassword(password);
//        personRepository.save(person);
//
//    }
    public boolean registrationPerson(RegistrationRequestDto dto) throws EmailAlreadyRegisteredException {
        Person exist = personRepository.findPersonByEmail(dto.getEmail()).orElse(null);
        if (exist != null) throw new EmailAlreadyRegisteredException();
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
        return true;
    }

    public ResponseDto login(LoginRequestDto dto){
        String email = dto.getEmail();
        String password = dto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)); //необходимо оставить
        Person person = personRepository.findPersonByEmail(email).orElse(null);//необходимо оставить
        if (person == null) {
            //
        }
        Token jwtToken = new Token();
        String token = tokenProvider.createToken(email);//необходимо оставить
        PersonDtoWithToken personDto = new PersonDtoWithToken();
        personDto.setPerson(person);
        personDto.setToken(token);

        jwtToken.setToken(token);
        jwtToken.setDateCreated(Calendar.getInstance());
        jwtToken.setPerson(person);
        Token t = tokenRepository.save(jwtToken);
        log.info(t.getPerson().getEmail());
        return new ResponseDto(personDto);
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

    public Person findPersonById(Integer id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null);
    }

    public boolean blockPersonById(Integer id) {
        Person person = findPersonById(id);
        if (person != null) {
            person.setBlocked(true);
            personRepository.save(person);
            return true;
        }
        return false;
    }

    public boolean unblockPersonById(Integer id) {
        Person person = findPersonById(id);
        if (person != null) {
            person.setBlocked(false);
            personRepository.save(person);
            return true;
        }
        return false;
    }

}
