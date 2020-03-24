package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.responseDto.FileUploadResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Post;
import project.repositories.PostRepository;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
public class ApiGeneralController {
    //liked, likes, feeds, notifications, tags, storage

    private PostRepository postRepository;

    private PersonService personService;

    @GetMapping
    public ResponseEntity<Iterable<Post>> feeds(){
        Iterable<Post> posts = postRepository.findAll();
        return ResponseEntity.ok().body(posts);
    }

    @PostMapping("storage")
    public ResponseEntity<FileUploadResponseDto> downloadImage(@RequestParam("type") String type, @RequestParam("file") MultipartFile file,
                                           HttpServletRequest request) throws BadRequestException400 {


        FileUploadResponseDto responseDto = personService.downloadImage(type, file, request);

        return ResponseEntity.ok().body(responseDto);
    }
}
