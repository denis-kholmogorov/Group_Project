package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import project.models.Token;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Integer> {

    Optional<Token> findByToken(String token);

    @Transactional
    void deleteByToken(String token);

    @Transactional
    void deleteByEmailUser(String email);
}
