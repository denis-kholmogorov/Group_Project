package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.CommentModelDto;
import project.dto.PostDto;
import project.dto.requestDto.PostRequestBodyTagsDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Person;
import project.models.Post;
import project.models.PostComment;
import project.security.TokenProvider;
import project.services.PostCommentsService;
import project.services.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api/v1/post")
@AllArgsConstructor
public class ApiPostController {
    private PostService postService;
    private PostCommentsService postCommentsService;
    private TokenProvider tokenProvider;

    @GetMapping("{id}")
    public ResponseEntity<ResponseDto<PostDto>> getPostById(@PathVariable Integer id) throws BadRequestException400 {
        PostDto postDto = postService.getPostDtoById(id, null);
        return ResponseEntity.ok(new ResponseDto<>(postDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseDto<PostDto>> editPostById(
            @PathVariable Integer id, @RequestParam(value = "publish_date", required = false) Long publishDate,
            @RequestBody PostRequestBodyTagsDto dto) throws BadRequestException400 {
        return ResponseEntity.ok(postService.editPostById(id, publishDate, dto));
    }

    //@Secured("ROLE_USER")
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDto<Integer>> deletePostById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.deletePostById(id));
    }

    //@Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<ListResponseDto<PostDto>> findPostsByTitleAndDate(
            @RequestParam String text,
            @RequestParam(required = false) Long date_from,
            @RequestParam(required = false) Long date_to,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer offset,
            @RequestParam(required = false, defaultValue = "20") @Positive @Max(20) Integer itemPerPage) {

        List<Post> posts = postService.getPostsByTitleAndDate(
                text, date_from + "", date_to + "", offset, itemPerPage);
        if (posts == null)
            return ResponseEntity.ok(new ListResponseDto<>(0L, offset, itemPerPage, new ArrayList<>()));

        List<PostDto> listPostsDto = posts.stream().map(post -> postService.getPostDtoById(post.getId(), null)).collect(toList());

        return ResponseEntity.ok(new ListResponseDto<>((long) posts.size(), offset, itemPerPage, listPostsDto));
    }

    @Secured("ROLE_USER")
    @GetMapping("{id}/comments")
    public ResponseEntity<?> getAllComments
            (@PathVariable(value = "id") Integer postId,
             @RequestParam(defaultValue = "0") Integer offsetParam,
             @RequestParam(defaultValue = "20") Integer limitParam){

        List<PostComment> comments = postCommentsService.getListComments(postId);

        ListResponseDto<PostComment> commentList = new ListResponseDto<>((long) comments.size(), offsetParam,
                limitParam, comments);

        return ResponseEntity.ok(commentList);
    }

    @Secured("ROLE_USER")
    @PostMapping("{id}/comments")
    public ResponseEntity<ResponseDto<PostComment>> addNewComent(@PathVariable(value = "id") Integer postId,
                                                                @RequestBody CommentModelDto commentModelDto,
                                                                HttpServletRequest servletRequest){

        Person person = tokenProvider.getPersonByRequest(servletRequest);
        PostComment postComment = postCommentsService.addNewCommentToPost(postId, commentModelDto, person);


        return ResponseEntity.ok(new ResponseDto<>(postComment));
    }

}
