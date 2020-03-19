package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.repositories.PostRepository;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;


}
