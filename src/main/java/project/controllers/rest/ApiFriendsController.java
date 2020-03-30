package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dto.responseDto.ListResponseDto;
import project.repositories.FriendshipRepository;
import project.services.FriendshipService;

@RestController
@RequestMapping(value = "/api/vi/friends/")
@AllArgsConstructor
public class ApiFriendsController {

    @Autowired
    private FriendshipRepository friendshipRepository;
    private FriendshipService friendshipService;

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
            @RequestParam String name, @RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "20") Integer itemPerPage) {
        return ResponseEntity.ok(friendshipService.getFriendList(name, offset, itemPerPage));
    }
}