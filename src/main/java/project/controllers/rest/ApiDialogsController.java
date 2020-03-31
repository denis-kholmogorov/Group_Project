package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.CountResponseDto;
import project.dto.dialog.request.CreateDialogDto;
import project.dto.dialog.request.MessageRequestDto;
import project.dto.dialog.response.DialogResponseDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Message;
import project.services.DialogService;
import project.services.MessageService;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;

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

        ListResponseDto answer = messageService.getAllDialogs(query, offset, itemPerPage, request);
        log.info(query + " Параметр query в контроллере Dialog");
        return ResponseEntity.ok(answer);
    }

    @Secured("ROLE_USER")
    @PostMapping()
    public ResponseEntity<?> createDialog (HttpServletRequest request,
                                             @RequestBody CreateDialogDto createDialogDto) throws BadRequestException400 {
        DialogResponseDto dialogResponseDto = messageService.createDialog(request, createDialogDto);
        return ResponseEntity.ok(new ResponseDto<>(dialogResponseDto));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/unreaded")
    public ResponseEntity<?> countSentMessage(HttpServletRequest servletRequest) throws BadRequestException400 {
        CountResponseDto responseDto = new CountResponseDto(messageService.getCountSendMessage(servletRequest));
        return ResponseEntity.ok(new ResponseDto<>(responseDto));
    }

    @Secured("ROLE_USER")
    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getDialogMessages(@PathVariable(value = "id") Integer id,
                                        @RequestParam(name = "query",required = false) String query,
                                        @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
                                        @RequestParam(name = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage,
                                        HttpServletRequest request){
        return ResponseEntity.ok(messageService.getDialogMessages(id, offset, itemPerPage, request));
    }

    @Secured("ROLE_USER")
    @PostMapping("/{id}/messages")
    public ResponseEntity<?> sentMessage(@PathVariable Integer id,
                                         @RequestBody MessageRequestDto dto,
                                         HttpServletRequest request){
        log.error("Отработал POst message");
        Message message = messageService.sentMessage(id,dto,request);
        return ResponseEntity.ok(new ResponseDto<>(message));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("{dialog_id}/messages/{message_id}/read")
    public ResponseEntity<?> readMessage(@PathVariable(value = "dialog_id") Integer dialogId,
                                         @PathVariable(value = "message_id") Integer messageId,
                                         HttpServletRequest request){
        messageService.readMessage(dialogId, messageId, request);
        return ResponseEntity.ok(new ResponseDto<>(new MessageResponseDto()));
    }
}
