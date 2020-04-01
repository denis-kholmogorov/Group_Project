package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.PostRequestBodyTagsDto;
import project.dto.requestDto.UpdatePersonDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.handlerExceptions.UnauthorizationException401;
import project.models.Person;
import project.security.TokenProvider;
import project.services.PersonService;
import project.services.PostService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users/")
@AllArgsConstructor
public class ApiUsersController {

    private PersonService personService;
    private PostService postService;
    private TokenProvider tokenProvider;

    @Secured("ROLE_USER")
    @GetMapping("me")
    public ResponseEntity<?> getAuthUser(ServletRequest servletRequest) throws UnauthorizationException401 {
        Person person = tokenProvider.getPersonByRequest((HttpServletRequest) servletRequest);

        person.setLastOnlineTime(new Date());
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @Secured("ROLE_USER")
    @PutMapping("me")
    public ResponseEntity<?> personEditBody(@RequestBody UpdatePersonDto updatePersonDto,
                                            HttpServletRequest request) throws UnauthorizationException401
    {
        Person person = personService.editBody(updatePersonDto, request);
        person.setLastOnlineTime(new Date());
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @Secured("ROLE_USER")
    @DeleteMapping("me")
    public ResponseEntity<?> deleteUser(ServletRequest servletRequest){
        Person person = personService.getPersonByToken(servletRequest);
        personService.deletePersonByEmail(person.getEmail());
        postService.deleteAllPostsByAuthorId(person.getId());
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

    @Secured("ROLE_USER")
    @GetMapping("{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Integer id) {
        Person person = personService.findPersonById(id);
        return ResponseEntity.ok(new ResponseDto<>(person));
    }

    @Secured("ROLE_USER")
    @GetMapping("{id}/wall")
    public ResponseEntity<?> getWallPostsById(
            @PathVariable Integer id, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage, ServletRequest servletRequest) throws BadRequestException400 {
        int compareId = tokenProvider.getPersonByRequest((HttpServletRequest) servletRequest).getId();
        return ResponseEntity.ok(postService.findAllByAuthorId(id, offset, itemPerPage, compareId));
    }

    @PostMapping("{id}/wall")
    public ResponseEntity<?> addWallPostById(
            @PathVariable Integer id, @RequestParam(value = "publish_date", required = false) Long publishDate,
            @RequestBody PostRequestBodyTagsDto dto) throws BadRequestException400 {
        return ResponseEntity.ok(postService.addNewWallPostByAuthorId(id, publishDate, dto));
    }

    @PutMapping("block/{id}")
    public ResponseEntity<?> blockPersonById(@PathVariable Integer id) throws BadRequestException400 {    //обработать 400 и 401
        personService.blockPersonById(id, true);
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

    @DeleteMapping("block/{id}")
    public ResponseEntity<?> unblockPersonById(@PathVariable Integer id) throws BadRequestException400 { //обработать 400 и 401
        personService.blockPersonById(id, false);
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }

    @GetMapping("search")
    ResponseEntity<?> search(@RequestParam(name = "first_name", required = false) @Size(max = 255) String firstName,
                             @RequestParam(name = "last_name", required = false) @Size(max = 255) String lastName,
                             @RequestParam(name = "age_from", required = false) Integer ageFrom,
                             @RequestParam(name = "age_to", required = false) Integer ageTo,
                             @RequestParam(required = false) @Size(max = 255) String country,
                             @RequestParam(required = false) @Size(max = 255) String city,
                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer offset,
                             @RequestParam(required = false, defaultValue = "20") @Positive @Max(20) Integer itemPerPage,
                             HttpServletRequest request) {
        Person person = tokenProvider.getPersonByRequest(request);
        long personCount = personService.searchCount(person, firstName, lastName, ageFrom, ageTo, country, city);
        List<Person> persons = personService.search(person, firstName, lastName, ageFrom, ageTo, country, city, offset, itemPerPage);
        return ResponseEntity.ok(new ListResponseDto<>(personCount, offset, itemPerPage, persons));
    }
}
