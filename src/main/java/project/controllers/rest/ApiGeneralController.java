package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.PostDto;
import project.dto.responseDto.FileUploadResponseDto;
import project.dto.responseDto.ListResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.services.PersonService;
import project.services.PostService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
public class ApiGeneralController {
    //liked, likes, feeds, notifications, tags, storage

    private PostService postService;
    private PersonService personService;

    @GetMapping("feeds")
    public ResponseEntity<ListResponseDto<PostDto>> feeds(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage)
            throws BadRequestException400 {
        return ResponseEntity.ok(postService.findAllPosts(name, offset, itemPerPage));
    }

    @PostMapping("storage")
    public ResponseEntity<FileUploadResponseDto> downloadImage(
            @RequestParam("type") String type, @RequestParam("file") MultipartFile file, HttpServletRequest request) {

        FileUploadResponseDto responseDto = personService.downloadImage(type, file, request);

        return ResponseEntity.ok().body(responseDto);
    }

}
