package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.LoginRequestDto;
import project.dto.LoginResponseDto;
import project.dto.PersonDto;
import project.dto.RegistrationRequestDto;
import project.models.Person;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

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

    public ResponseEntity<Person> registrationPerson(RegistrationRequestDto dto){
        Optional<Person> optional = personRepository.findPersonByEmail(dto.getEmail());
        if (optional.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Person person = new Person();
//        Person.PersonBuilder personBuilder = Person.builder()
//                .email(dto.getEmail())
//                .password(dto.getPassword())
//                .firstName(dto.getFirstName())
//                .lastName(dto.getLastName())
//                .regDate(LocalDateTime.now());
//        Person person = personRepository.save(personBuilder.build());

        return ResponseEntity.ok(person);

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

    public PersonDto getPersonDtoById(Integer id){
        Person personFromDb = personRepository.findById(id).orElse(null);
        if(personFromDb != null) {

          return new PersonDto(personFromDb.getId(), personFromDb.getFirstName(),
                    personFromDb.getLastName(), personFromDb.getRegDate(), personFromDb.getBirthDate(),
                    personFromDb.getEmail(), personFromDb.getPhone(), personFromDb.getPhoto(), personFromDb.getAbout(),
                    personFromDb.getTown(), personFromDb.getCountry(), personFromDb.getMessagePermission(),
                    personFromDb.getLastOnlineTime(), personFromDb.getIsBlocked());
        }
        return null;
    }
}
