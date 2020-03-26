package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.PostDto;
import project.dto.requestDto.PostRequestBodyDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Post;
import project.services.PostService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api/v1/post/")
@AllArgsConstructor
public class ApiPostController {
    private PostService postService;

    @Secured("ROLE_USER")
    @GetMapping("{id}")
    public ResponseEntity<ResponseDto<PostDto>> getPostById(@PathVariable Integer id) throws BadRequestException400 {
        PostDto postDto = postService.getPostDtoById(id, null);
        return ResponseEntity.ok(new ResponseDto<>(postDto));
    }

    @Secured("ROLE_USER")
    @PutMapping("{id}")
    public ResponseEntity<ResponseDto<PostDto>> editPostById(
            @PathVariable Integer id, @RequestParam(value = "publish_date", required = false) Long publishDate,
            @RequestBody PostRequestBodyDto dto) throws BadRequestException400 {
        return ResponseEntity.ok(postService.editPostById(id, publishDate, dto));
    }

    @Secured("ROLE_USER")
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDto<Integer>> deletePostById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.deletePostById(id));
    }

    @Secured("ROLE_USER")
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


}
