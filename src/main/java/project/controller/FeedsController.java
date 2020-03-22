package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.models.Post;
import project.repositories.PostRepository;

import java.util.List;

@RestController
@RequestMapping("/feeds")
@CrossOrigin(origins="*")
public class FeedsController {

    private PostRepository postRepository;

    @Autowired
    public FeedsController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Post>> feeds(){
        Iterable<Post> posts = postRepository.findAll();
        return null;
    }
}
