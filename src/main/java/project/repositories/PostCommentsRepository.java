package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.PostComments;

@Repository
public interface PostCommentsRepository extends CrudRepository<PostComments, Integer>
{
}
