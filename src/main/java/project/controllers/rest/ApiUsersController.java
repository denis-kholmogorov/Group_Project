package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.NewWallPostDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.security.TokenProvider;
import project.services.PersonService;
import project.services.PostService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    public ResponseEntity<?> addWallPostById(@PathVariable Integer id, @RequestBody NewWallPostDto dto) {   //обработать 400 и 401

        return null;
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
