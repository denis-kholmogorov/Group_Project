package project.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Friendship;

import java.util.List;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {
    @Query("select f from Friendship f " +
            "join FriendshipStatus fs on fs.id = f.statusId " +
            "where (f.personIdWhoSendFriendship = :personId OR f.personIdWhoTakeFriendship = :personId) " +
            "AND fs.code = 'FRIEND'")
    List<Friendship> findAllByPersonIdWhoSendFriendshipOrPersonIdWhoTakeFriendshipAndStatus
            (Integer personId, Pageable pageable);
}
