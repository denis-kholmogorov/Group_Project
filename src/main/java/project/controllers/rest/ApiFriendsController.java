package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dto.responseDto.ListResponseDto;
import project.models.Person;
import project.repositories.FriendshipRepository;
import project.security.TokenProvider;
import project.services.FriendshipService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/friends")
@AllArgsConstructor
public class ApiFriendsController {

    @Autowired
    private FriendshipRepository friendshipRepository;
    private FriendshipService friendshipService;
    private TokenProvider tokenProvider;

//        @GetMapping
//        ResponseEntity<?> friends(@RequestParam(name = "query") String query, FriendParamsDto friendParams) throws EntityValidationException {
//
//            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//            Validator validator = factory.getValidator();
//
//            Set<ConstraintViolation<FriendParamsDto>> violations = validator.validate(friendParams);
//
//            if (violations.size() != 0) {
//                throw new EntityValidationException(FriendParamsDto.class, new EntityViolation<>(violations).toString());
//            }
//
//            return ResponseEntity.ok().build();
//        }

    @GetMapping
    public ResponseEntity<ListResponseDto> getFriendList(
            @RequestParam(required = false) String name, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage, ServletRequest servletRequest) {
        Person person = tokenProvider.getPersonByRequest((HttpServletRequest) servletRequest);
        return ResponseEntity.ok(friendshipService.getFriendList(name, offset, itemPerPage, person));
    }
}