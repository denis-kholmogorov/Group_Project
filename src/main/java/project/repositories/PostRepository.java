package project.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.dto.responseDto.ListResponseDto;
import project.models.Post;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAllByAuthorId(Integer authorId, Pageable pageable);
    List<Post> findAllByTitleContainingAndTimeBetween(String title, Date dateFrom, Date dateTo, Pageable pageable);
    List<Post> findAllByTitleContainingAndTimeBefore(String title, Date date, Pageable pageable);
    List<Post> findAllByTitleContainingAndTimeAfter(String title, Date date, Pageable pageable);
    List<Post> findAllByTimeBetween(Date dateFrom, Date dateTo, Pageable pageable);
    List<Post> findAllByTimeAfter(Date date, Pageable pageable);
    List<Post> findAllByTimeBefore(Date date, Pageable pageable);
    List<Post> findAllByTitleContaining(String title, Pageable pageable);
    List<Post> findAll(Pageable pageable);
 }
