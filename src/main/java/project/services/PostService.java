package project.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.models.Post;
import project.repositories.PostRepository;

import java.util.List;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;

    public Post getPostById(Integer id){
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public List<Post> findAllByAuthorId(Integer authorId, Integer offset, Integer limit) {
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        return postRepository.findAllByAuthorId(authorId, pageable);
    }
}
