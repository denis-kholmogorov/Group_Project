package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.CommentDto;
import project.dto.PostDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.models.Post;
import project.models.ResponseModel;
import project.services.PersonService;
import project.services.PostCommentsService;
import project.services.PostLikeService;
import project.services.PostService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api/v1/post/")
@AllArgsConstructor
public class ApiPostController {
    private PostService postService;
    private PersonService personService;
    private PostLikeService postLikeService;
    private PostCommentsService postCommentsService;

    @GetMapping("{id}")
    public ResponseEntity<ResponseDto<PostDto>> getPostById(@PathVariable Integer id){
        PostDto postDto = getPostDtoById(id);
        return ResponseEntity.ok(new ResponseDto<>(postDto));
    }

    @GetMapping
    public ResponseEntity<ListResponseDto<ResponseDto<PostDto>>>
    findPostsByTitleAndDate(@RequestParam String text,
                            @RequestParam String dateFrom,
                            @RequestParam String dateTo,
                            @RequestParam Integer offsetParam,
                            @RequestParam Integer limitParam){

        List<Post> posts = postService.getPostsByTitleAndDate(text, dateFrom, dateTo, offsetParam, limitParam);
        List<ResponseDto<PostDto>> listPostsDto = posts.stream().map(post -> {
            PostDto postDto = getPostDtoById(post.getId());
            return new ResponseDto<>(postDto);
        }).collect(toList());

        return ResponseEntity.ok(new ListResponseDto<>(new ResponseModel(), posts.size(),
                offsetParam, limitParam, listPostsDto));
    }

    private PostDto getPostDtoById(Integer id) {
        Post post = postService.getPostById(id);
        Person person = personService.findPersonById(post.getAuthorId());

        Integer countLikes = postLikeService.countLikesByPostId(post.getId());

        List<CommentDto> comments = postCommentsService.getListCommentsDto(post.getId());

        return new PostDto(post.getId(), post.getTime(), person, post.getTitle(),
                post.getPostText(), post.getIsBlocked(), countLikes, comments);
    }
}
