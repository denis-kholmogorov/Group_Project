package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.PostDto;
import project.dto.requestDto.PostRequestBodyDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Post;
import project.services.PostService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api/v1/post/")
@AllArgsConstructor
public class ApiPostController {
    private PostService postService;

    @GetMapping("{id}")
    public ResponseEntity<ResponseDto<PostDto>> getPostById(@PathVariable Integer id){
        PostDto postDto = postService.getPostDtoById(id);
        return ResponseEntity.ok(new ResponseDto<>(postDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseDto<PostDto>> editPostById(
            @PathVariable Integer id, @RequestParam(value = "publish_date", required = false) Long publishDate, @RequestBody PostRequestBodyDto dto) {
        return ResponseEntity.ok(postService.editPostById(id, publishDate, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDto<Integer>> deletePostById(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.deletePostById(id));
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
            PostDto postDto = postService.getPostDtoById(post.getId());
            return new ResponseDto<>(postDto);
        }).collect(toList());

        return ResponseEntity.ok(new ListResponseDto<>(posts.size(),
                offsetParam, limitParam, listPostsDto));
    }


}
