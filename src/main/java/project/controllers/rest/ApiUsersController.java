package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.NewWallpostDto;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.models.enums.MessagesPermission;
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
    public ResponseEntity<?> getAuthUser(ServletRequest servletRequest){
        String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);
        String email = tokenProvider.getUserEmail(token);
        Person person = personService.findPersonByEmail(email);

        person.setAbout("1");
        person.setPhone("1");
        person.setPhoto("1");
        person.setBirthDate(new Date());
        person.setLastOnlineTime(new Date());
        person.setRegDate(new Date());
        person.setMessagesPermission(MessagesPermission.ALL);
        person.setCity("1");
        person.setCountry("1");
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Integer id) {
        Person person = personService.findPersonById(id);
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @GetMapping("{id}/wall")
    public ResponseEntity<?> getWallPostsById(@PathVariable Integer id, Integer offsetParam, Integer limitParam) {
        return ResponseEntity.ok(postService.findAllByAuthorId(id, offsetParam, limitParam));
    }

    @PostMapping("{id}/wall")
    public ResponseEntity<?> addWallPostById(@PathVariable Integer id, @RequestBody NewWallpostDto dto) {
        return null;
    }

    @PutMapping("block/{id}")
    public ResponseEntity<?> blockPersonById(@PathVariable Integer id) {
        if (personService.blockPersonById(id, true)) return ResponseEntity.ok(null);
        else return ResponseEntity.status(400).body(null); //нужна? обработка 400 и 401
    }

    @DeleteMapping("block/{id}")
    public ResponseEntity<?> unblockPersonById(@PathVariable Integer id) {
        if (personService.blockPersonById(id, false)) return ResponseEntity.ok(null);
        else return ResponseEntity.status(400).body(null); //нужна? обработка 400 и 401
    }

}
