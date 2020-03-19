package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.CommentDto;
import project.dto.PersonDto;
import project.dto.PostDto;
import project.dto.ResponseDto;
import project.models.Post;
import project.services.PersonService;
import project.services.PostCommentsService;
import project.services.PostLikeService;
import project.services.PostService;

import java.util.List;

@RestController
@RequestMapping(value = "/post/")
@AllArgsConstructor
public class ApiPostController {
    private PostService postService;
    private PersonService personService;
    private PostLikeService postLikeService;
    private PostCommentsService postCommentsService;

    @GetMapping("{id}")
    public ResponseEntity<ResponseDto> getPostById(@PathVariable Integer id){
        Post post = postService.getPostById(id);
        PersonDto personDto = personService.getPersonDtoById(post.getAuthorId());

        Integer countLikes = postLikeService.countLikesByPostId(post.getId());

        List<CommentDto> comments = postCommentsService.getListCommentsDto(post.getId());

        PostDto postDto = new PostDto(post.getId(), post.getTime(), personDto, post.getTitle(), post.getPostText(),
                post.getIsBlocked(), countLikes, comments);
        return ResponseEntity.ok(new ResponseDto("", "123213241", postDto));
    }
}
