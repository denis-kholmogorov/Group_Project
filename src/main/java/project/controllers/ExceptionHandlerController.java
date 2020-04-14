package project.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.dto.error.Error;
import project.dto.error.enums.ErrorDescriptionEnum;
import project.dto.error.enums.ErrorEnum;
import project.handlerExceptions.BadRequestException400;

import javax.validation.ConstraintViolationException;


@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

/** контроллер отлавливает ошибку 400*/

    @ExceptionHandler(BadRequestException400.class)
    public ResponseEntity<?> badRequestException() {
        Error error = new Error(ErrorEnum.INVALID_REQUEST.getError(), ErrorDescriptionEnum.BAD_REQUEST.getError());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

   /* @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handlerNotFound(Exception e){

        return ResponseEntity.status(404).body("Not Found");
    }*/
}


