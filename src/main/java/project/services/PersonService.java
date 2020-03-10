package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.repositories.PersonRepository;

@Service
@AllArgsConstructor
public class PersonService {
    private PersonRepository personRepository;
}
