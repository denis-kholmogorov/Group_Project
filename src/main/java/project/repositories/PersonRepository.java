package project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.models.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

    Optional<Person> findByEmail(String email);

    Optional<Person> findById(int id);

     @Query("Select p from Person p where p.email = :email")
     Optional<Person> findPersonByEmail(String email);

     @Transactional
     void deleteByEmail(String email);

}
