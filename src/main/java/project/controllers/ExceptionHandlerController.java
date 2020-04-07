package project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.dto.error.Error;
import project.dto.error.enums.ErrorDescriptionEnum;
import project.dto.error.enums.ErrorEnum;
import project.handlerExceptions.BadRequestException400;


@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

/** контроллер отлавливает ошибку 400*/

    @ExceptionHandler(BadRequestException400.class)
    public ResponseEntity<?> badRequestException() {
        Error error = new Error(ErrorEnum.INVALID_REQUEST.getError(), ErrorDescriptionEnum.BAD_REQUEST.getError());
        return ResponseEntity.status(400).body(error);
    }
}


