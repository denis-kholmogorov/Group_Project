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
import project.repositories.MessageRepository;
import project.repositories.PersonRepository;
import project.security.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageService {

    private MessageRepository messageRepository;

    private TokenProvider tokenProvider;

    private PersonService personService;

    private PersonRepository personRepository;

    private DialogService dialogService;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          TokenProvider tokenProvider,
                          PersonService personService,
                          PersonRepository personRepository,
                          DialogService dialogService) {
        this.messageRepository = messageRepository;
        this.tokenProvider = tokenProvider;
        this.personService = personService;
        this.personRepository = personRepository;
        this.dialogService = dialogService;
    }

    public ListResponseDto<DialogDto> getAllDialogs(String query, Integer offset, Integer itemPerPage,
                                         HttpServletRequest request) throws BadRequestException400 {
        //Pageable pageable = PageRequest.of((offset / itemPerPage), itemPerPage);
        Person person = tokenProvider.getPersonByRequest(request);

        Integer count = messageRepository.countByRecipientIdAndReadStatus(person.getId(), ReadStatus.SENT);

        int toIndex = offset + itemPerPage;
        int listSize =  person.getDialogs().size();
        List<Dialog> dialogList = person.getDialogs().subList(offset, Math.min(toIndex, listSize)); //dialogService.getAllDialogs(paging);
        List<DialogDto> dialogDtoList = dialogList.stream().map(dialog -> {
            DialogDto dialogDto = new DialogDto();
            dialogDto.setId(dialog.getId());
            dialogDto.setUnreadCount(count);
            Message message = dialog.getListMessage().get(dialog.getListMessage().size() - 1);
            MessageDto messageDto = new MessageDto();
            messageDto.setId(message.getId());
            messageDto.setAuthorId(message.getAuthorId());
            messageDto.setMessageText(message.getMessageText());
            messageDto.setTime(message.getTime());
            messageDto.setReadStatus(message.getReadStatus());
            Person recipient = personService.findPersonById(message.getRecipientId());
            messageDto.setRecipient(recipient);
            dialogDto.setMessage(messageDto);
            return dialogDto;
        }).collect(Collectors.toList());

        return new ListResponseDto<>((long) dialogDtoList.size(), offset, itemPerPage, dialogDtoList);
    }

    public DialogResponseDto createDialog(HttpServletRequest request, DialogUserShortList userIds) throws BadRequestException400 {
        userIds.getUserIds().forEach(id->log.info(id+""));
        Person person = tokenProvider.getPersonByRequest(request); // как этого чувака привязать к диалогам
        Dialog dialog = new Dialog();

        Iterable<Person> personIterable = personRepository.findAllById(userIds.getUserIds());

        dialog.setPersons((Set<Person>) personIterable);
        Integer id = dialogService.saveDialog(dialog).getId();

        return new DialogResponseDto(id);
    }

    public Integer getCountSendMessage(HttpServletRequest request) throws BadRequestException400 {
        Person recipient = tokenProvider.getPersonByRequest(request);
        return messageRepository
                .countByRecipientIdAndReadStatus(recipient.getId(), ReadStatus.SENT);
    }

    public ListResponseDto getDialogMessages(
            Integer id, Integer offset, Integer itemPerPage, HttpServletRequest servletRequest) {

        Pageable pageable = PageRequest.of((offset / itemPerPage), itemPerPage);
        List<Message> dialogMessages = messageRepository.findAllByDialogId(id, pageable);
        List<MessageDto> messageDtoList = dialogMessages.stream().distinct().map(message -> {
            MessageDto messageDto = new MessageDto();
            messageDto.setId(message.getId());
            //Person author = personService.findPersonById(message.getRecipientId());
            messageDto.setAuthorId(message.getAuthorId());
            messageDto.setMessageText(message.getMessageText());
            messageDto.setTime(message.getTime());
            messageDto.setReadStatus(message.getReadStatus());
            Person recipient = personService.findPersonById(message.getRecipientId());
            messageDto.setRecipient(recipient);
            return messageDto;
        }).collect(Collectors.toList());
        return new ListResponseDto<>((long) messageDtoList.size(), offset, itemPerPage, messageDtoList);
    }

    public Message sentMessage(Integer id, MessageRequestDto dto, HttpServletRequest request) throws BadRequestException400 {
        Person person = tokenProvider.getPersonByRequest(request);
        Dialog dialog = dialogService.findById(id);
        Message message = new Message();
        message.setTime(new Date());
        message.setAuthorId(person.getId());
        message.setRecipientId(person.getId());
        message.setMessageText(dto.getMessageText());
        message.setReadStatus(ReadStatus.SENT);
        message.setDialog(dialog);
        Message messageSaved = messageRepository.save(message);
        dialog.getListMessage().add(message);
        dialogService.saveDialog(dialog);

        return messageSaved;
    }
}
