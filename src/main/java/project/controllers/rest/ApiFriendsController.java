package project.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.handlerExceptions.EntityValidationException;
import project.models.util.entity.EntityViolation;
import project.dto.requestDto.FriendParamsDto;
import project.repositories.FriendshipRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/vi/friends/")
@AllArgsConstructor
public class ApiFriendsController {

        @Autowired
        private FriendshipRepository friendshipRepository;

        @GetMapping
        ResponseEntity<?> friends(@RequestParam(name = "query") String query, FriendParamsDto friendParams) throws EntityValidationException {

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<FriendParamsDto>> violations = validator.validate(friendParams);

            if (violations.size() != 0) {
                throw new EntityValidationException(FriendParamsDto.class, new EntityViolation<>(violations).toString());
            }

            return ResponseEntity.ok().build();
        }
}