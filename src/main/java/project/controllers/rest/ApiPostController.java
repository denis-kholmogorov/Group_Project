package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.CommentDto;
import project.dto.CommentModelDto;
import project.dto.PostDto;
import project.dto.requestDto.PostRequestBodyDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Person;
import project.models.Post;
import project.models.PostComment;
import project.security.TokenProvider;
import project.services.PostCommentsService;
import project.services.PostService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api/v1/post/")
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
            @RequestBody PostRequestBodyDto dto) throws BadRequestException400 {
        return ResponseEntity.ok(postService.editPostById(id, publishDate, dto));
    }

    //@Secured("ROLE_USER")
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDto<Integer>> deletePostById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.deletePostById(id));
    }

    //@Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<ListResponseDto<ResponseDto<PostDto>>> findPostsByTitleAndDate(
            @RequestParam String text,
            @RequestParam(required = false) Long dateFrom,
            @RequestParam(required = false) Long dateTo,
            @RequestParam(required = false) Integer offsetParam,
            @RequestParam(required = false) Integer limitParam) throws BadRequestException400 {

        List<Post> posts = postService.getPostsByTitleAndDate(
                text, dateFrom + "", dateTo + "", offsetParam, limitParam);
        if (posts == null) throw new BadRequestException400();
        List<ResponseDto<PostDto>> listPostsDto = posts.stream().map(post ->
                new ResponseDto<>(postService.getPostDtoById(post.getId(), null))).collect(toList());

        return ResponseEntity.ok(new ListResponseDto<>((long) posts.size(), offsetParam, limitParam, listPostsDto));
    }

    @Secured("ROLE_USER")
    @GetMapping("{id}/comments")
    public ResponseEntity<?> getAllComments
            (@PathVariable(value = "id") Integer postId,
             @RequestParam(required = false) Integer offsetParam,
             @RequestParam(required = false) Integer limitParam){

        List<CommentDto> commentDtoList = postCommentsService.getListCommentsDto(postId);

        ListResponseDto<CommentDto> commentList = new ListResponseDto<>((long)commentDtoList.size(), offsetParam,
                limitParam, commentDtoList);

        return ResponseEntity.ok(commentList);
    }

    @Secured("ROLE_USER")
    @PostMapping("{id}/comments")
    public ResponseEntity<ResponseDto<CommentDto>> addNewComent(@PathVariable(value = "id") Integer postId,
                                                                @RequestBody CommentModelDto commentModelDto,
                                                                ServletRequest servletRequest){

        Person person = tokenProvider.getPersonByRequest((HttpServletRequest) servletRequest);
        PostComment postComment = postCommentsService.addNewCommentToPost(postId, commentModelDto, person.getId());


        return ResponseEntity.ok
                (new ResponseDto<>(new CommentDto(commentModelDto.getParentId(), commentModelDto.getCommentText(),
                        postComment.getId(), postId, postComment.getTime(), postComment.getAuthorId(), postComment.getIsBlocked())));
    }

}
