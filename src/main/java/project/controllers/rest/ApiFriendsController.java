package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.repositories.FriendshipRepository;
import project.security.TokenProvider;
import project.services.FriendshipService;
import project.services.PersonService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/friends")
@Slf4j
public class ApiFriendsController {

    private FriendshipService friendshipService;
    private TokenProvider tokenProvider;
    private PersonService personService;

    @Autowired
    public ApiFriendsController(FriendshipService friendshipService, TokenProvider tokenProvider, PersonService personService) {
        this.friendshipService = friendshipService;
        this.tokenProvider = tokenProvider;
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<ListResponseDto> getFriendList(
            @RequestParam(required = false) String name, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage, ServletRequest servletRequest) {
        Person person = tokenProvider.getPersonByRequest((HttpServletRequest) servletRequest);
        return ResponseEntity.ok(friendshipService.getFriendList(name, offset, itemPerPage, person));
    }

    @PostMapping("/{id}")
    public ResponseDto<String> sendFriendRequest(@PathVariable Integer id, HttpServletRequest request) throws Exception {
        friendshipService.sendFriendshipRequest(id, request);
        return new ResponseDto<>("ok");
    }

    @DeleteMapping("/{id}")
    public ResponseDto<String> deleteFriend(@PathVariable Integer id, HttpServletRequest request){
        friendshipService.deleteFriend(id, request);
        return new ResponseDto<>("ok");
    }

    @GetMapping("/request")
    public ResponseEntity getFriendRequests(
            @RequestParam(required = false, name = "name") String name, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage, HttpServletRequest request){

        Person person = tokenProvider.getPersonByRequest(request);
        log.info(person.getLastName());
        return ResponseEntity.ok(friendshipService.getFriendRequests(name, offset, itemPerPage, person));
    }

    @GetMapping("recommendations")
    ResponseEntity<?> recommendations(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer offset,
                                   @RequestParam(required = false, defaultValue = "20") @Positive @Max(20) Integer itemPerPage,
                                   HttpServletRequest request) {
        Person person = tokenProvider.getPersonByRequest(request);
        long total = personService.recommendationsCount(person);
        List<Person> list = personService.recommendations(person, offset, itemPerPage);
        ListResponseDto<Person> response = new ListResponseDto<>(total, offset, itemPerPage, list);
        return ResponseEntity.ok(response);
    }
}