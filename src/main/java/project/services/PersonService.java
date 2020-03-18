package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import project.dto.AuthRequest;
import project.dto.RegistrationRequest;
import project.models.Person;
import project.repositories.PersonRepository;

import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    private PersonRepository personRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;

    @Autowired
    public PersonService(PersonRepository personRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager) {
        this.personRepository = personRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Person loginPerson(@RequestBody AuthRequest authRequest){

        String email = authRequest.getEmail();
        String password = authRequest.getPassword();
        Optional<Person> personOptional = personRepository.findByEmail(email);
        if (personOptional.isPresent()){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            log.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()));
            return personOptional.get();
        }
        return null;
    }

    public void registerPerson(@RequestBody RegistrationRequest request){

        Person person = new Person();
        log.info("request: " + request.toString());
        log.info(request.getPasswd1());
        request.setPasswd1(bCryptPasswordEncoder.encode(request.getPasswd1()));
        person.setEmail(request.getEmail());
        person.setPassword(request.getPasswd1());
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        personRepository.save(person);
    }
}
