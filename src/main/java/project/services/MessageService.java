package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.dialog.request.CreateDialogDto;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final TokenProvider tokenProvider;

    private final PersonService personService;

    private final PersonRepository personRepository;

    private final DialogRepository dialogRepository;

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

    public ListResponseDto<DialogDto> getAllDialogs(String query, Integer offset, Integer itemPerPage,
                                         HttpServletRequest request) throws BadRequestException400 {
        Person person = tokenProvider.getPersonByRequest(request);
        int toIndex = offset + itemPerPage;
        int listSize =  person.getDialogs().size();
        List<Dialog> dialogList = person.getDialogs().subList(offset, Math.min(toIndex, listSize));
        List<DialogDto> dialogDtoList = dialogList.stream().map(dialog -> {
            DialogDto dialogDto = new DialogDto();
            dialogDto.setId(dialog.getId());
            dialogDto.setUnreadCount(messageRepository.countByRecipientIdAndReadStatus(person.getId(), ReadStatus.SENT));
            Message message = null;
            if(dialog.getListMessage().size() == 0) {
                message = sentMessage(
                        dialog.getId(),
                        new MessageRequestDto("Привет! Я " + person.getFirstName() + " " + person.getLastName()),
                        request);
            }else {
                message = dialog.getListMessage().get(dialog.getListMessage().size() - 1);
            }
            dialogDto.setMessage(MessageDto.builder()
                    .id(message.getId())
                    .author(personRepository.findById(message.getAuthorId()).orElseThrow(BadRequestException400::new))
                    .recipient(personRepository.findById(message.getRecipientId()).orElseThrow(BadRequestException400::new))
                    .readStatus(message.getReadStatus())
                    .messageText(message.getMessageText())
                    .sentByMe(message.getAuthorId() == person.getId())
                    .time(message.getTime())
                    .build());
            return dialogDto;
        }).collect(Collectors.toList());

        return new ListResponseDto<>((long) dialogDtoList.size(), offset, itemPerPage, dialogDtoList);
    }

    public DialogResponseDto createDialog(HttpServletRequest request, CreateDialogDto userIds) {
        Person personAuthor = tokenProvider.getPersonByRequest(request); // как этого чувака привязать к диалогам
        Dialog dialog = new Dialog();

        Person personRecipient = personRepository.findById(userIds.getUserIds().get(0)).orElse(null);

        dialog.getPersons().add(personAuthor);
        dialog.getPersons().add(personRecipient);
        Integer id = dialogRepository.save(dialog).getId();
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
        Person author = tokenProvider.getPersonByRequest(servletRequest);
        List<Message> dialogMessages = messageRepository.findAllByDialogId(id, pageable);
        List<MessageDto> messageDtoList = dialogMessages.stream().distinct().map(message -> MessageDto.builder()
               .id(message.getId())
               .author(personRepository.findById(message.getAuthorId()).orElseThrow(BadRequestException400::new))
               .recipient(personRepository.findById(message.getRecipientId()).orElseThrow(BadRequestException400::new))
               .readStatus(message.getReadStatus())
               .messageText(message.getMessageText())
               .sentByMe(message.getAuthorId() == author.getId())
               .time(message.getTime())
               .build()).collect(Collectors.toList());
        return new ListResponseDto<>((long) messageDtoList.size(), offset, itemPerPage, messageDtoList);
    }

    public Message sentMessage(Integer id, MessageRequestDto dto, HttpServletRequest request) throws BadRequestException400 {
        Person person = tokenProvider.getPersonByRequest(request);
        Dialog dialog = dialogRepository.findById(id).orElseThrow(BadRequestException400::new);
        Set<Person> personsSet = dialog.getPersons();
        List<Person> personList = new ArrayList<>(personsSet);
        personList.remove(person);
        Message message = new Message();
        message.setTime(new Date());
        message.setAuthorId(personList.get(0).getId()); // Перепутаны на фронте
        message.setRecipientId(person.getId());         // Перепутаны на фронте
        message.setMessageText(dto.getMessageText());
        message.setReadStatus(ReadStatus.SENT);
        message.setDialog(dialog);
        Message messageSaved = messageRepository.save(message);
        dialog.getListMessage().add(message);
        dialogRepository.save(dialog);

        return messageSaved;
    }

    public void readMessage(Integer dialogId, Integer messageId, HttpServletRequest request){
       Message message = messageRepository.findByIdAndDialogId(messageId, dialogId).orElseThrow(BadRequestException400::new);
       log.info(message.getMessageText() + " Прочитана");
       message.setReadStatus(ReadStatus.READ);
       messageRepository.save(message);
    }

}
