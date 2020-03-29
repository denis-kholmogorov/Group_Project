package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.dialog.request.DialogUserShortList;
import project.dto.dialog.response.DialogDto;
import project.dto.dialog.response.DialogResponseDto;
import project.dto.responseDto.ListResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Dialog;
import project.models.Person;
import project.repositories.DialogRepository;
import project.repositories.MessageRepository;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class MessageService {

    private MessageRepository messageRepository;

    private TokenProvider tokenProvider;

    private PersonRepository personRepository;

    private DialogRepository dialogRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          TokenProvider tokenProvider,
                          PersonRepository personRepository,
                          DialogRepository dialogRepository) {
        this.messageRepository = messageRepository;
        this.tokenProvider = tokenProvider;
        this.personRepository = personRepository;
        this.dialogRepository = dialogRepository;
    }

    public ListResponseDto getAllMessages(String query, Integer offset, Integer itemPerPage,
                                          HttpServletRequest request) throws BadRequestException400 {
        Pageable paging = PageRequest.of((offset / itemPerPage), itemPerPage);
        //Person person = tokenProvider.getPersonByRequest(request);

        Iterable<Dialog> dialogList = dialogRepository.findAll();
        List<DialogDto> dialogDtoList = new ArrayList<>();
        dialogList.forEach(dialog -> {
            DialogDto dialogDto = new DialogDto();
            dialogDto.setId(dialog.getId());
            dialogDto.setUnreadCount(dialog.getUnread().size());
            dialogDto.setMessage(dialog.getListMessage().get(0));
            dialogDtoList.add(dialogDto);
        });

        return new ListResponseDto((long) dialogDtoList.size(), offset, itemPerPage, dialogDtoList);
//        List<Message> messageList = messageRepository.findAllByAuthorIdOrRecipientId(person.getId(), paging);
//        MessageDto message = new MessageDto();
//        message.setAuthorId(person.getId());
//        message.setId(1);
//        message.setMessageText("Hellow workd");
//        message.setReadStatus(ReadStatus.SENT);
//        message.setRecipientId(2);
//        message.setTime(Calendar.getInstance().getTime().getTime());
//        List<DialogDto> dialogs = new ArrayList<>();
//        DialogDto dialogDto = new DialogDto(message);
//        dialogs.add(dialogDto);
//        return new DialogsResponseDto(dialogs);
    }

    public DialogResponseDto createDialog(HttpServletRequest request, DialogUserShortList userIds) throws BadRequestException400 {
        userIds.getUserIds().forEach(id->log.info(id+""));
        Person person = tokenProvider.getPersonByRequest(request); // как этого чувака привязать к диалогам
        Dialog dialog = new Dialog();

        Iterable<Person> personIterable = personRepository.findAllById(userIds.getUserIds());

        dialog.setPersons((Set<Person>) personIterable);
        Integer id = dialogRepository.save(dialog).getId();

        return new DialogResponseDto(id);
    }

}
