package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
            return personOptional.get();
        }
        return null;
    }

    public void registerPerson(@RequestBody RegistrationRequest request){

        Person person = new Person();
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        person.setEmail(request.getEmail());
        person.setPassword(request.getPassword());
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        personRepository.save(person);
    }
}
