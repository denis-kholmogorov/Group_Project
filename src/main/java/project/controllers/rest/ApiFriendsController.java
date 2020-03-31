package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.repositories.FriendshipRepository;
import project.security.TokenProvider;
import project.services.FriendshipService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/friends")
@AllArgsConstructor
@Slf4j
public class ApiFriendsController {

    @Autowired
    private FriendshipRepository friendshipRepository;
    private FriendshipService friendshipService;
    private TokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity<ListResponseDto> getFriendList(
            @RequestParam(required = false) String name, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage, ServletRequest servletRequest) {
        Person person = tokenProvider.getPersonByRequest((HttpServletRequest) servletRequest);
        return ResponseEntity.ok(friendshipService.getFriendsList(name, offset, itemPerPage, person));
    }

    @PostMapping("/{id}")
    public ResponseDto<String> sendFriendRequest(@PathVariable Integer id, HttpServletRequest request) {
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
}