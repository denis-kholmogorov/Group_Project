package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Post2Tag;

@Repository
public interface Post2TagRepository extends CrudRepository<Post2Tag, Integer> {
}
