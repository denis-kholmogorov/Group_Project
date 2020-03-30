package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.dto.PostDto;
import project.dto.responseDto.FileUploadResponseDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Image;
import project.models.util.entity.ImagePath;
import project.models.Person;
import project.security.TokenProvider;
import project.services.GeneralService;
import project.services.PersonService;
import project.services.PostService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
@AllArgsConstructor
public class ApiGeneralController {
    //liked, likes, feeds, notifications, tags, storage

    private PostService postService;
    private PersonService personService;
    private GeneralService generalService;
    private TokenProvider tokenProvider;
    private ImagePath imagePath;

    @GetMapping("feeds")
    public ResponseEntity<ListResponseDto<PostDto>> feeds(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage)
            throws BadRequestException400 {
        return ResponseEntity.ok(postService.findAllPosts(name, offset, itemPerPage));
    }

    @PostMapping("storage")
    ResponseEntity<?> upload(@RequestParam String type,
                             @RequestPart("file") MultipartFile multipartFile,
                             HttpServletRequest request) throws IOException {
        FileUploadResponseDto response = new FileUploadResponseDto();

        if (type.equals("IMAGE")) {
            if (multipartFile.isEmpty())
                throw  new BadRequestException400();

            response.setFileName(multipartFile.getOriginalFilename());
            response.setRelativeFilePath("");
            response.setFileFormat(multipartFile.getContentType());
            response.setBytes(multipartFile.getSize());
            response.setFileType("IMAGE");
            response.setCreatedAt(new Date().getTime());

            Person person = tokenProvider.getPersonByRequest(request);
            response.setOwnerId(person.getId());
            String photo = person.getPhoto();
            if (photo.equals(imagePath.getDefaultImagePath())) {
                Integer id = generalService.saveImage(multipartFile.getBytes(), multipartFile.getContentType());
                response.setId(String.valueOf(id));
                String URL = imagePath.getImagePath()  + id;
                response.setRawFileURL(URL);
                personService.updatePhoto(person, URL);
            } else {
                Integer oldId = Integer.valueOf(photo.replace(imagePath.getImagePath(), ""));
                response.setId(String.valueOf(oldId));
                response.setRawFileURL(imagePath.getImagePath());
                generalService.updateImage(multipartFile.getBytes(), multipartFile.getContentType(), oldId);
            }
        } else {
            throw new BadRequestException400();
        }

        return ResponseEntity.ok(new ResponseDto<>(response));
    }

    @GetMapping(value = "storage/{id}")
    ResponseEntity<?> download(@PathVariable Integer id) {
        Image image = generalService.findImage(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getType())).body(image.getImage());
    }

    @GetMapping(value = "storage/default", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody byte[] downloadDefault() throws IOException {
        Resource resource = new ClassPathResource("images/default-user-image.png");
        return IOUtils.toByteArray(resource.getInputStream());
    }
}