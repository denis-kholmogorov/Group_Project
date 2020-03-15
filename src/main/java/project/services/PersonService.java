package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.LoginRequestDto;
import project.dto.LoginResponseDto;
import project.dto.RegistrationRequestDto;
import project.models.Person;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

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


    public boolean registrationPerson(RegistrationRequestDto dto){
        return false;

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
