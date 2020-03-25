package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dto.requestDto.OffsetLimitDto;
import project.handlerExceptions.BadRequestException400;
import project.services.MessageService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/dialogs")
@AllArgsConstructor
public class ApiDialogsController {

    @Autowired
    MessageService messageService;

    @GetMapping()
    public ResponseEntity<?> getAllDialogs(@RequestParam(name = "query") String query,
                                                                         OffsetLimitDto dto,
                                                                         HttpServletRequest request) throws BadRequestException400 {
        ResponseEntity answer = messageService.getAllMessages(query, dto, request);
        log.info(query + " Параметр query в контроллере Dialog");
        return null;
    }
}
