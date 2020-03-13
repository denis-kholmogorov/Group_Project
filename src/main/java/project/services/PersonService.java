package project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.LoginRequestDto;
import project.dto.RegistrationRequestDto;
import project.models.Person;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
@AllArgsConstructor
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TokenProvider tokenProvider;

    public boolean registrationPerson(RegistrationRequestDto dto){
        Person person = new Person();
        person.setEmail(dto.getEmail());
        person.setPassword(encoder.encode(dto.getPassword()));
        Person p = personRepository.save(person);
        if(p != null) return true;
        return false;

    }

    public Person login(LoginRequestDto dto){
        Person person = personRepository.findPersonByEmail(dto.getEmail()).orElse(null);
        log.info(person.getPassword());
        if(person != null && encoder.matches(dto.getPassword(), person.getPassword())){
            return person;
        }
        return null;
    }

    public Person findPersonByEmail(String email){
        Person person = personRepository.findPersonByEmail(email).get();
        if(person == null){log.info("Юзер не найден!! в юзер сервисе"); return null; }
        return person;
    }
}
