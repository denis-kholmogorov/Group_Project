package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
}
