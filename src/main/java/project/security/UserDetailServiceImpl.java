package project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.models.Person;
import project.repositories.PersonRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService
{

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    public UserDetailServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personRepository.findPersonByEmail(email).get();
        if(person != null){
            return person;
        }
        throw new UsernameNotFoundException("Person with " + email + " not found");
    }
}
