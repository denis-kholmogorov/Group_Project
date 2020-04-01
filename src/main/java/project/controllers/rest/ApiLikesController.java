package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.AddLike;
import project.dto.responseDto.ResponseDto;
import project.models.Person;
import project.models.PostLike;
import project.security.TokenProvider;
import project.services.PostLikeService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/likes")
@AllArgsConstructor
public class ApiLikesController {

    private PostLikeService postLikeService;
    private TokenProvider tokenProvider;

    @Secured("ROLE_USER")
    @PutMapping
    public ResponseEntity<?> takeLikeTo(@RequestBody AddLike addLike, ServletRequest servletRequest){

        Person person = tokenProvider.getPersonByRequest((HttpServletRequest) servletRequest);

        PostLike postLike = postLikeService.addLike(person.getId(), addLike.getItem_id());

        return ResponseEntity.ok(new ResponseDto<>(postLike));
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    public ResponseEntity<?> unLike(@RequestBody AddLike addLike, ServletRequest servletRequest){

        Person person = tokenProvider.getPersonByRequest((HttpServletRequest) servletRequest);

        Integer likeCount = postLikeService.deleteLike(addLike.getItem_id(), person.getId());

        return ResponseEntity.ok(new ResponseDto<>(likeCount));
    }

}
