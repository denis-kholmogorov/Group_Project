package project.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.models.util.exception.EntityValidationException;

@ControllerAdvice
public class EntityValidationHandler {

    @ExceptionHandler(EntityValidationException.class)
    void handle(Exception e) {
        e.printStackTrace();
    }
}
