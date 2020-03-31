package project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

    @Transactional
    Optional<Person> findByEmail(String email);

    @Transactional
    Optional<Person> findById(int id);

    @Query("Select p from Person p where p.email = :email")
    Optional<Person> findPersonByEmail(String email);

     @Query("Select p from Person p where p.email = :email")
     Optional<Person> findPersonByEmail(String email);

    void deleteByEmail(String email);

    // Поиск друзей пользователя (не нужен после добавления связей в Person, к удалению)
    @Query(value = "select p from Person p " +
            "join Friendship f on (f.dstPerson = p OR f.srcPerson = p) " +
            "join FriendshipStatus fs on fs = f.status " +
            "where (f.srcPerson = :person OR f.dstPerson = :person) " +
            "AND fs.code = 'FRIEND' AND NOT p = :person")
    List<Person> findFriends(Person person);

    // Поиск заявок в друзья пользователя (не нужен после добавления связей в Person, к удалению)
    @Query(value = "select p from Person p " +
            "join Friendship f on f.dstPerson = p " +
            "join FriendshipStatus fs on fs = f.status " +
            "where f.dstPerson = :person " +
            "AND fs.code = 'REQUEST'")
    List<Person> findFriendRequests(Person person);

    List<Person> findAllByFirstName(String name);
}
