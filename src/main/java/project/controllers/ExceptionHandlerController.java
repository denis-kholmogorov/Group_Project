package project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.dto.error.Error;
import project.dto.error.enums.ErrorDescriptionEnum;
import project.dto.error.enums.ErrorEnum;
import project.handlerExceptions.BadRequestException400;
import project.handlerExceptions.NotFoundException404;
import project.handlerExceptions.UnauthorizationException401;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    /** контроллер отлавливает ошибку 400*/
    @ExceptionHandler(BadRequestException400.class)
    public ResponseEntity<?> badRequestException() {
        Error error = new Error(ErrorEnum.INVALID_REQUEST.getError(), ErrorDescriptionEnum.BAD_REQUEST.getError());
        return ResponseEntity.status(400).body(error);
    }

    /** контроллер отлавливает ошибку 401*/
    @ExceptionHandler(UnauthorizationException401.class)
    public ResponseEntity<?> unauthorized(){
        Error error = new Error(ErrorEnum.UNAUTHORIZED.getError(), ErrorDescriptionEnum.UNAUTHORIZED.getError());
        return ResponseEntity.status(401).body(error);
    }

/*    *//** контроллер отлавливает ошибку 404*//*
    @ExceptionHandler(NotFoundException404.class)
    public ResponseEntity<?> notFound(){
        Error error = new Error(ErrorEnum.INVALID_REQUEST.getError(), ErrorDescriptionEnum.CODE_SUPPLIED.getError());
        return ResponseEntity.status(404).body(error);
    }*/

}

