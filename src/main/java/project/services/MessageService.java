package project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.dialog.request.DialogUserShortList;
import project.dto.dialog.response.DialogResponseDto;
import project.dto.dialog.response.DialogsResponseDto;
import project.dto.requestDto.OffsetLimitDto;
import project.handlerExceptions.BadRequestException400;
import project.models.Dialog;
import project.models.Message;
import project.models.Person;
import project.models.enums.ReadStatus;
import project.repositories.DialogRepository;
import project.repositories.MessageRepository;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public DialogsResponseDto getAllMessages(String query, Integer offset, Integer itemPerPage, HttpServletRequest request) throws BadRequestException400 {


        Pageable paging = PageRequest.of((offset / itemPerPage), itemPerPage);
        Person person = tokenProvider.getPersonByRequest(request);
        List<Message> messageList = messageRepository.findAllByAuthorIdOrRecipientId(person.getId(), paging);
        Dialog dialog = new Dialog();
        dialog.setId(1);
        dialog.getPersons().add(person);
        Message message = new Message();
        message.setAuthorId(person.getId());
        message.setId(1);
        message.setMessageText("Hellow workd");
        message.setReadStatus(ReadStatus.READ);
        message.setRecipientId(2);
        message.setTime(LocalDateTime.now());
        message.setDialog(dialog);
        List<Dialog> dialogs = new ArrayList<>();
        dialog.getListMessage().add(message);
        dialogs.add(dialog);
        return new DialogsResponseDto(dialogs);
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
