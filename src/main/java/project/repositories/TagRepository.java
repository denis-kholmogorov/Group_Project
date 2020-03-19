package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
}
