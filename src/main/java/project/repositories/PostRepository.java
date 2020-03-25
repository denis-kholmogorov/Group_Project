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

    List<Post> findAllByTitleContainingAndTimeBetweenAndIsBlocked(String title, Date dateFrom,
                                                                  Date dateTo, Boolean isBlocked, Pageable pageable);

    List<Post> findAllByTitleContainingAndTimeBeforeAndIsBlocked(String title, Date date,
                                                                 Boolean isBlocked, Pageable pageable);

    List<Post> findAllByTitleContainingAndTimeAfterAndIsBlocked(String title, Date date,
                                                                Boolean isBlocked, Pageable pageable);

    List<Post> findAllByTimeBetweenAndIsBlocked(Date dateFrom, Date dateTo, Boolean isBlocked, Pageable pageable);

    List<Post> findAllByTimeAfterAndIsBlocked(Date date, Boolean isBlocked, Pageable pageable);

    List<Post> findAllByTimeBeforeAndIsBlocked(Date date, Boolean isBlocked, Pageable pageable);

    List<Post> findAllByTitleContainingAndIsBlocked(String title, Boolean isBlocked, Pageable pageable);

    List<Post> findAllByIsBlocked(Boolean isBlocked, Pageable pageable);
}
