package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.PostLike;

@Repository
public interface PostLikeRepository extends CrudRepository <PostLike, Integer> {
}
