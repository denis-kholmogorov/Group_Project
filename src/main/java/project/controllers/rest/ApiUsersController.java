package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.PostRequestBodyDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.security.TokenProvider;
import project.services.PersonService;
import project.services.PostService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users/")
@AllArgsConstructor
public class ApiUsersController {

    private PersonService personService;
    private PostService postService;
    private TokenProvider tokenProvider;

    @GetMapping("me")
    public ResponseEntity<?> getAuthUser(ServletRequest servletRequest){    //обработать 401
        String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);
        String email = tokenProvider.getUserEmail(token);
        Person person = personService.findPersonByEmail(email);

        person.setLastOnlineTime(new Date());
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @PutMapping("me")
    public ResponseEntity<?> changeUser(ServletRequest servletRequest){
        log.info("Put отработал");
       /* String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);
        String email = tokenProvider.getUserEmail(token);
        Person person = personService.findPersonByEmail(email);

        person.setLastOnlineTime(new Date());
        return ResponseEntity.ok(new ResponseDto<>(person));*/
        return ResponseEntity.ok("Все ок");
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Integer id) {  //обработать 401
        Person person = personService.findPersonById(id);
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @GetMapping("{id}/wall")
    public ResponseEntity<?> getWallPostsById(@PathVariable Integer id, @RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "20") Integer itemPerPage) {  //обработать 400 и 401
        return ResponseEntity.ok(postService.findAllByAuthorId(id, offset, itemPerPage));
    }

    @PostMapping("{id}/wall")
    public ResponseEntity<?> addWallPostById(@PathVariable Integer id, @RequestParam(value = "publish_date", required = false) Long publishDate, @RequestBody PostRequestBodyDto dto) {   //обработать 400 и 401
        return ResponseEntity.ok(postService.addNewWallPostByAuthorId(id, publishDate, dto));
    }

    @PutMapping("block/{id}")
    public ResponseEntity<?> blockPersonById(@PathVariable Integer id) {    //обработать 400 и 401
        personService.blockPersonById(id, true);
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

    @DeleteMapping("block/{id}")
    public ResponseEntity<?> unblockPersonById(@PathVariable Integer id) { //обработать 400 и 401
        personService.blockPersonById(id, false);
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

}
