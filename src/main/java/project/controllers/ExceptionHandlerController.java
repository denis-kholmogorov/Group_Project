package project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.dto.error.Error;
import project.dto.error.enums.ErrorDescriptionEnum;
import project.dto.error.enums.ErrorEnum;
import project.handlerExceptions.EmailAlreadyRegisteredException;
import project.handlerExceptions.UnauthorizationException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    /** ЕМАЙЛ уже есть БД*/
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<?> handleEmailAlreadyRegisteredException() {
        Error error = new Error(ErrorEnum.INVALID_REQUEST.getError(), ErrorDescriptionEnum.BAD_CREDENTIALS.getError());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(UnauthorizationException.class)
    public ResponseEntity<?> unauthorizated(){
        Error error = new Error(ErrorEnum.UNAUTHORIZED.getError(), ErrorDescriptionEnum.UNAUTHORIZED.getError());
        return ResponseEntity.status(400).body(error);
    }

}

