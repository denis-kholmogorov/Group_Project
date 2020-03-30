package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.dialog.request.DialogUserShortList;
import project.dto.dialog.request.MessageRequestDto;
import project.dto.dialog.response.DialogDto;
import project.dto.dialog.response.DialogResponseDto;
import project.dto.dialog.response.MessageDto;
import project.dto.responseDto.ListResponseDto;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class MessageService {

    private MessageRepository messageRepository;

    private TokenProvider tokenProvider;

    private PersonService personService;

    private PersonRepository personRepository;

    private DialogRepository dialogRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          TokenProvider tokenProvider,
                          PersonService personService,
                          PersonRepository personRepository,
                          DialogRepository dialogRepository) {
        this.messageRepository = messageRepository;
        this.tokenProvider = tokenProvider;
        this.personService = personService;
        this.personRepository = personRepository;
        this.dialogRepository = dialogRepository;
    }

    public ListResponseDto getAllMessages(String query, Integer offset, Integer itemPerPage,
                                          HttpServletRequest request) throws BadRequestException400 {
        Pageable paging = PageRequest.of((offset / itemPerPage), itemPerPage);
        Person user = tokenProvider.getPersonByRequest(request);

        Integer count = messageRepository.countByRecipientIdAndReadStatus(user.getId(), ReadStatus.SENT);

        Iterable<Dialog> dialogList = dialogRepository.findAll();
        List<DialogDto> dialogDtoList = new ArrayList<>();
        dialogList.forEach(dialog -> {
            DialogDto dialogDto = new DialogDto();
            dialogDto.setId(dialog.getId());
            dialogDto.setUnreadCount(count);
            Message message = dialog.getListMessage().get(dialog.getListMessage().size() -1);
            MessageDto messageDto = new MessageDto();
            messageDto.setId(message.getId());
            messageDto.setAuthorId(message.getAuthorId());
            messageDto.setMessageText(message.getMessageText());
            messageDto.setTime(message.getTime());
            messageDto.setReadStatus(message.getReadStatus());
            Person person = personService.findPersonById(message.getRecipientId());
            messageDto.setRecipient(person);
            dialogDto.setMessage(messageDto);
            dialogDtoList.add(dialogDto);
        });

        return new ListResponseDto((long) dialogDtoList.size(), offset, itemPerPage, dialogDtoList);
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

    public Integer getCountSendMessage(HttpServletRequest request) throws BadRequestException400 {
        Person recipient = tokenProvider.getPersonByRequest(request);
        return messageRepository
                .countByRecipientIdAndReadStatus(recipient.getId(), ReadStatus.SENT);
    }

    public ListResponseDto getDialogMessages(
            Dialog dialog, Integer offset, Integer itemPerPage, HttpServletRequest servletRequest) {

        return null;
    }

    public Message sentMessage(Integer id, MessageRequestDto dto, HttpServletRequest request) throws BadRequestException400 {
        Person person = tokenProvider.getPersonByRequest(request);
        Dialog dialog = dialogRepository.findById(id).get();
        Message message = new Message();
        message.setTime(new Date());
        message.setAuthorId(person.getId());
        message.setRecipientId(person.getId());
        message.setMessageText(dto.getMessageText());
        message.setReadStatus(ReadStatus.SENT);
        message.setDialog(dialog);
        Message messageSaved = messageRepository.save(message);
        dialog.getListMessage().add(message);
        dialogRepository.save(dialog);

        return messageSaved;
    }
}
