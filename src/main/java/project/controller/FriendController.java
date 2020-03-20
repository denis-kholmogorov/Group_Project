package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.models.util.entity.EntityViolation;
import project.models.util.exception.EntityValidationException;
import project.models.util.entity.FriendParams;
import project.repositories.FriendshipRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@RestController
@RequestMapping("api/v1/friends")
public class FriendController {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @GetMapping
    ResponseEntity<?> friends(FriendParams friendParams) throws EntityValidationException {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<FriendParams>> violations = validator.validate(friendParams);

        if (violations.size() != 0) {
            throw new EntityValidationException(FriendParams.class, new EntityViolation<>(violations).toString());
        }

        return ResponseEntity.ok().build();
    }
}
