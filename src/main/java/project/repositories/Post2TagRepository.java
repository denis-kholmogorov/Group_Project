package project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.models.Post2Tag;

import java.util.List;

@Repository
public interface Post2TagRepository extends CrudRepository<Post2Tag, Integer> {
    List<Post2Tag> findAllByPostId(Integer id);
}
