package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.models.Post;
import project.repositories.PostRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;


    public Post getPostById(Integer id){
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}
