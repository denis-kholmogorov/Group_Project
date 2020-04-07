package project.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.models.Tag;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    List<Tag> findAllByTagContaining(String tag, Pageable pageable);

    @Transactional
    void deleteById(Integer id);

}
