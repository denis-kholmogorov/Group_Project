package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Friendship;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {
}
