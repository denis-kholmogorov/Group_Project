package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dto.PostDto;
import project.dto.responseDto.ListResponseDto;
import project.services.PostService;

@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
public class ApiGeneralController {
    //liked, likes, feeds, notifications, tags, storage

    private PostService postService;

    @GetMapping("feeds")
    public ResponseEntity<ListResponseDto<PostDto>> feeds(@RequestParam(required = false) String name,
                                                          @RequestParam(defaultValue = "0") Integer offset,
                                                          @RequestParam(defaultValue = "20") Integer itemPerPage){
        return ResponseEntity.ok(postService.findAllPosts(name, offset, itemPerPage));
    }
}
