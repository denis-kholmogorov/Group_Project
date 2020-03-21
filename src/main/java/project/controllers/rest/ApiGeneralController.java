package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.models.Post;
import project.repositories.PostRepository;

@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
public class ApiGeneralController {
    //liked, likes, feeds, notifications, tags, storage

    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<Iterable<Post>> feeds(){
        Iterable<Post> posts = postRepository.findAll();
        return ResponseEntity.ok().body(posts);
    }
}
