package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dto.responseDto.FileUploadResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Image;
import project.models.Person;
import project.models.Post;
import project.repositories.PostRepository;
import project.services.GeneralService;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
public class ApiGeneralController {
    //liked, likes, feeds, notifications, tags, storage

    private PostRepository postRepository;

    private PersonService personService;

    private GeneralService generalService;

    @GetMapping
    public ResponseEntity<Iterable<Post>> feeds(){
        Iterable<Post> posts = postRepository.findAll();
        return ResponseEntity.ok().body(posts);
    }

    /*@PostMapping("storage")
    public ResponseEntity<FileUploadResponseDto> downloadImage(@RequestParam("type") String type, @RequestParam("file") MultipartFile file,
                                           HttpServletRequest request) throws BadRequestException400 {

        FileUploadResponseDto responseDto = personService.downloadImage(type, file, request);

        return ResponseEntity.ok().body(responseDto);
    }*/

    @PostMapping("storage")
    ResponseEntity<?> upload(@RequestParam String type, @RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException, BadRequestException400 {
        if (type.equals("IMAGE")) {
            Integer id = generalService.saveImage(multipartFile.getBytes());
            personService.updatePersonImage(request, String.valueOf(id));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "storage/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody byte[] download(@PathVariable Integer id) {
        return generalService.getImage(id).getImage();
    }
}
