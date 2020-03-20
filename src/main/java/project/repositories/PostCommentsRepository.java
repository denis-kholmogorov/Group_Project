package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.PostComments;

import java.util.List;

@Repository
public interface PostCommentsRepository extends CrudRepository<PostComments, Integer>
{
    List<PostComments> findAllByPostIdAndIsBlocked(Integer postId, boolean isBlocked);
}
