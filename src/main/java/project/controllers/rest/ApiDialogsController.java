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
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Message;
import project.services.MessageService;

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
                                           HttpServletRequest request)
    {DialogsResponseDto answer = null;
        try {
            answer = messageService.getAllMessages(query, offset, itemPerPage, request);
        }catch (BadRequestException400 e){
            log.info("400");
        }

        log.info(query + " Параметр query в контроллере Dialog");
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping()
    public ResponseEntity<?> wcreationDialogue (HttpServletRequest request, @RequestBody DialogUserShortList dialogUserShortList) throws BadRequestException400 {
        DialogResponseDto dialogResponseDto = messageService.createDialog(request, dialogUserShortList);
        return ResponseEntity.ok(new ResponseDto<>(dialogResponseDto));
    }


}
