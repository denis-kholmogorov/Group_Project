package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
}
