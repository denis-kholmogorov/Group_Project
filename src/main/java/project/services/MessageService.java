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
import project.models.*;
import project.models.enums.NotificationTypeEnum;
import project.models.enums.ReadStatus;
import project.repositories.*;
import project.security.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final TokenProvider tokenProvider;

    private final PersonService personService;

    private final PersonRepository personRepository;

    private final DialogRepository dialogRepository;

    private final NotificationTypeRepository notificationTypeRepository;

    private final NotificationRepository notificationRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          TokenProvider tokenProvider,
                          PersonService personService,
                          PersonRepository personRepository,
                          DialogRepository dialogRepository, NotificationTypeRepository notificationTypeRepository, NotificationRepository notificationRepository) {
        this.messageRepository = messageRepository;
        this.tokenProvider = tokenProvider;
        this.personService = personService;
        this.personRepository = personRepository;
        this.dialogRepository = dialogRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.notificationRepository = notificationRepository;
    }

    public Message findMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
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
            dialogDto.setUnreadCount(messageRepository.countByAuthorIdAndReadStatusAndDialogId(person.getId(),
                    ReadStatus.SENT,
                    dialog.getId()));
            return dialogDto;
        }).collect(Collectors.toList());


        return new ListResponseDto<>((long) dialogDtoList.size(), offset, itemPerPage, dialogDtoList);
    }

    public DialogResponseDto createDialog(HttpServletRequest request, CreateDialogDto userIds) {
        Person personAuthor = tokenProvider.getPersonByRequest(request); // как этого чувака привязать к диалогам
        Person personRecipient = personRepository.findById(userIds.getUserIds().get(0)).orElse(null);
        AtomicReference<Boolean> exist = new AtomicReference<>(false);
        AtomicReference<Integer> existDialog = new AtomicReference<>() ;

        List<Dialog> list = personAuthor.getDialogs();
        if (list.size() != 0) {
            list.stream().forEach(dialog -> {
                if (dialog.getPersons().contains(personRecipient)) {
                    existDialog.set(dialog.getId());
                    exist.set(true);
                }
            });
            if (exist.get()) {
                return new DialogResponseDto(existDialog.get());
            }
        }

        Dialog dialog = new Dialog();
        dialog.getPersons().add(personAuthor);
        dialog.getPersons().add(personRecipient);
        Integer id = dialogRepository.save(dialog).getId();
        return new DialogResponseDto(id);
    }

    public Integer getCountSendMessage(HttpServletRequest request) throws BadRequestException400 {
        Person recipient = tokenProvider.getPersonByRequest(request);
        return messageRepository
                .countByAuthorIdAndReadStatus(recipient.getId(), ReadStatus.SENT);
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
        Person author = tokenProvider.getPersonByRequest(request);
        Dialog dialog = dialogRepository.findById(id).orElseThrow(BadRequestException400::new);
        List<Person> personList = new ArrayList<>(dialog.getPersons());
        personList.remove(author);
        Person recipient = personList.get(0);

        Message message = new Message();
        message.setTime(new Date());
        message.setAuthorId(author.getId()); // Перепутаны на фронте
        message.setRecipientId(recipient.getId());         // Перепутаны на фронте
        message.setMessageText(dto.getMessageText());
        message.setReadStatus(ReadStatus.SENT);
        message.setDialog(dialog);
        Message messageSaved = messageRepository.save(message);
        //dialog.getListMessage().add(message);
        //dialogRepository.save(dialog);

        Notification notification = new Notification();
        NotificationType notificationType = notificationTypeRepository.findByCode(NotificationTypeEnum.MESSAGE);
        notification.setPerson(recipient);
        notification.setContact("Contact");
        notification.setMainEntity(message);
        notification.setNotificationType(notificationType);
        notification.setSentTime(new Date());
        notificationRepository.save(notification);

        return messageSaved;
    }

    public void readMessage(Integer dialogId, Integer messageId, HttpServletRequest request){
       Message message = messageRepository.findByIdAndDialogId(messageId, dialogId).orElseThrow(BadRequestException400::new);
       log.info(message.getMessageText() + " Прочитана");
       message.setReadStatus(ReadStatus.READ);
       messageRepository.save(message);
    }

}
