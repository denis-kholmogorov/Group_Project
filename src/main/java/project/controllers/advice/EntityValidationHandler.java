package project.controllers.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.handlerExceptions.EntityValidationException;

@ControllerAdvice
public class EntityValidationHandler {

    @ExceptionHandler(EntityValidationException.class)
    void handle(Exception e) {
        e.printStackTrace();
    }
}
