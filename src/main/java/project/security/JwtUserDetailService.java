package project.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.models.Person;
import project.models.Role;
import project.repositories.PersonRepository;
import project.services.PersonService;

/** Используется для работы с Security, т.к. Security должен работать с UserDetails*/

@Slf4j
@Service
public class JwtUserDetailService implements UserDetailsService
{

    private final PersonService personService;

    private final PersonRepository personRepository;

    @Autowired
    public JwtUserDetailService(PersonService personService, PersonRepository personRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personService.findPersonByEmail(email);
        if(person == null){
            throw new UsernameNotFoundException("Person with " + email + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(person);
        log.info("In load by user with email " + jwtUser.getEmail());
        return jwtUser;
    }
}
