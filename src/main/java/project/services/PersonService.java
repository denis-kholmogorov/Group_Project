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
import project.models.VerificationToken;
import project.repositories.PersonRepository;
import project.util.EmailServiceImpl;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PersonService {

    private PersonRepository personRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;
    private EmailServiceImpl emailService;
    private VerificationTokenService verificationTokenService;

    @Autowired
    public PersonService(PersonRepository personRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                         AuthenticationManager authenticationManager, EmailServiceImpl emailService, VerificationTokenService verificationTokenService) {
        this.personRepository = personRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
    }

    public Person findByEmail(String email){
        return personRepository.findByEmail(email).orElse(null);
    }

    public Person findById(int id){
        return personRepository.findById(id).orElse(null);
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

    public void registerPerson(RegistrationRequest request){

        Person person = new Person();
        request.setPasswd1(bCryptPasswordEncoder.encode(request.getPasswd1()));
        person.setEmail(request.getEmail());
        person.setPassword(request.getPasswd1());
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        personRepository.save(person);
    }

    public void sendRecoveryPasswordEmail(String email) {

        Person person = findByEmail(email);
        if (person != null) {
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken(token, person.getId(), 20);
            String link = "http://localhost:8080/account/password/set/" + token;
            String message = String.format("Для восстановления пароля перейдите по ссылке %s", link );
            verificationTokenService.save(verificationToken);
            emailService.send(email, "Password recovery", message);
        }
    }

    public void setNewPassword(String tokenFromEmail, String password){

        log.info(tokenFromEmail);
        VerificationToken verificationToken = verificationTokenService.findByUUID(tokenFromEmail);
        log.info(String.valueOf(verificationToken == null));
        if (verificationToken != null && (new Date().before(verificationToken.getExpirationDate()))) {
            int personId = verificationToken.getUserId();
            Person person = findById(personId);
            if (person != null){
                log.info(person.toString());
                log.info(password);
                password = bCryptPasswordEncoder.encode(password);
                log.info(password);
                person.setPassword(password);
                personRepository.save(person);
            }
        }
    }
}
