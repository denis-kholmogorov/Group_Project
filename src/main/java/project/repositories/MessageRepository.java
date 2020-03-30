package project.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Message;
import project.models.enums.ReadStatus;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>
{
    @Query(value ="SELECT m FROM Message m WHERE m.authorId = :authorId OR m.recipientId = :authorId")
    List<Message> findAllByAuthorIdOrRecipientId(Integer authorId, Pageable pageable);

    Integer countByRecipientIdAndReadStatus(Integer recipientId, ReadStatus readStatus);

    List<Message> findAllByDialogId(Integer dialogId, Pageable pageable);
}
