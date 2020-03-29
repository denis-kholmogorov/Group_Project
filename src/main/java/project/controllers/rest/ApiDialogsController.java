package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.dialog.request.DialogUserShortList;
import project.dto.dialog.response.DialogResponseDto;
import project.dto.dialog.response.DialogsResponseDto;
import project.dto.dialog.response.MessageDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.enums.ReadStatus;
import project.services.MessageService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/dialogs")
@AllArgsConstructor
public class ApiDialogsController {

    @Autowired
    MessageService messageService;

    @Secured("ROLE_USER")
    @GetMapping()
    public ResponseEntity<?> getAllDialogs(@RequestParam(name = "query",required = false) String query,
                                           @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
                                           @RequestParam(name = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage,
                                           HttpServletRequest request) {
        ListResponseDto answer = messageService.getAllMessages(query, offset, itemPerPage, request);


        log.info(query + " Параметр query в контроллере Dialog");
        return ResponseEntity.ok(answer);
    }

    @Secured("ROLE_USER")
    @PostMapping()
    public ResponseEntity<?> creationDialogue (HttpServletRequest request, @RequestBody DialogUserShortList dialogUserShortList) throws BadRequestException400 {
        DialogResponseDto dialogResponseDto = messageService.createDialog(request, dialogUserShortList);
        return ResponseEntity.ok(new ResponseDto<>(dialogResponseDto));
    }

    @Secured("ROLE_USER")
    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getMessage(@PathVariable("id") Integer id,
                                        @RequestParam(name = "query",required = false) String query,
                                        @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
                                        @RequestParam(name = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage,
                                        HttpServletRequest request){
        MessageDto message = new MessageDto();
        message.setAuthorId(1);
        message.setId(1);
        message.setMessageText("Hello world");
        message.setReadStatus(ReadStatus.SENT);
        message.setRecipientId(1);
        message.setTime(Calendar.getInstance().getTime().getTime());
        List<MessageDto> messages = new ArrayList<>();
        messages.add(message);
        return ResponseEntity.ok(new DialogsResponseDto(messages) );
    }

}
