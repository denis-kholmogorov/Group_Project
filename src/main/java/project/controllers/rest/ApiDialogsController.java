package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.CountResponseDto;
import project.dto.dialog.request.DialogUserShortList;
import project.dto.dialog.request.MessageRequestDto;
import project.dto.dialog.response.DialogResponseDto;
import project.dto.dialog.response.MessageDto;
import project.dto.dialog.response.MessageDto2;
import project.dto.dialog.response.MessageSetResponseDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Dialog;
import project.models.Message;
import project.models.Person;
import project.services.DialogService;
import project.services.MessageService;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/dialogs")
@AllArgsConstructor
public class ApiDialogsController {

    @Autowired
    PersonService personService;

    @Autowired
    MessageService messageService;

    @Autowired
    DialogService dialogService;

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
    @GetMapping(value = "/unreaded")
    public ResponseEntity<?> countSentMessage(HttpServletRequest servletRequest) throws BadRequestException400 {
        CountResponseDto responseDto = new CountResponseDto(messageService.getCountSendMessage(servletRequest));
        return ResponseEntity.ok(new ResponseDto<>(responseDto));
    }

    @Secured("ROLE_USER")
    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getMessages(@PathVariable(value = "id") Integer id,
                                        @RequestParam(name = "query",required = false) String query,
                                        @RequestParam(name = "offset", required = false, defaultValue = "0") Integer offset,
                                        @RequestParam(name = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage,
                                        HttpServletRequest request){
//        MessageDto message = new MessageDto();
//        message.setAuthorId(1);
//        message.setId(1);
//        message.setMessageText("Hello world");
//        message.setReadStatus(ReadStatus.SENT);
//        message.setRecipientId(1);
//        message.setTime(Calendar.getInstance().getTime().getTime());
//        List<MessageDto> messages = new ArrayList<>();
//        messages.add(message);

        Dialog dialog = dialogService.findById(id);
        if (dialog == null) throw new BadRequestException400();
        List<Message> dialogMessages = dialog.getListMessage();
        List<MessageDto2> messageDtoList = dialogMessages.stream().map(message -> {
            MessageDto2 messageDto = new MessageDto2();
            messageDto.setId(message.getId());
            Person author = personService.findPersonById(message.getRecipientId());
            messageDto.setAuthor(author);
            messageDto.setMessageText(message.getMessageText());
            messageDto.setTime(message.getTime());
            messageDto.setReadStatus(message.getReadStatus());
            Person recipient = personService.findPersonById(message.getRecipientId());
            messageDto.setRecipient(recipient);
            return messageDto;
        }).collect(Collectors.toList());
        Set<MessageRequestDto> map = new HashSet(messageDtoList);
        return ResponseEntity.ok(
                new MessageSetResponseDto<>((long) messageDtoList.size(), offset, itemPerPage, map));
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
}
