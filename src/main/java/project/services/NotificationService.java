package project.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.PostDto;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.NotificationDto;
import project.models.*;
import project.models.enums.NotificationTypeEnum;
import project.repositories.NotificationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
public class NotificationService {

    private NotificationRepository notificationRepository;
    private PersonService personService;
    private MessageService messageService;
    private PostService postService;
    private PostCommentsService postCommentsService;



    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               PersonService personService,
                               MessageService messageService,
                               PostService postService,
                               PostCommentsService postCommentsService) {
        this.notificationRepository = notificationRepository;
        this.personService = personService;
        this.messageService = messageService;
        this.postService = postService;
        this.postCommentsService = postCommentsService;
    }

    public ListResponseDto<NotificationDto> findAllNotificationsByPersonId(
            Person person, Integer offset, Integer itemsPerPage) {
        List<Notification> notificationList = person.getNotificationList().subList(
                offset, Math.min(itemsPerPage, person.getNotificationList().size()));
        List<NotificationDto> notificationDtoList = notificationList.stream().map(notification -> {
            NotificationDto notificationDto = new NotificationDto();
            NotificationTypeEnum notificationTypeCode = notification.getNotificationType().getCode();
            Integer entityId = notification.getMainEntity().getId();


            switch (notificationTypeCode) {
                case POST:
                    PostDto postDto = postService.getPostDtoById(entityId, null);
                    notificationDto.setMainEntity(postDto.getAuthor());
                    notificationDto.setInfo(postDto.getTitle());
                    break;
                case POST_COMMENTS:
                case COMMENT_COMMENT:
                    break;
                case FRIEND_BIRTHDAY:
                    break;
                case FRIEND_REQUEST:
                    Person personResponse = personService.findPersonById(entityId);
                    notificationDto.setMainEntity(personResponse);
                    notificationDto.setInfo(personResponse.getFirstName() + " " + personResponse.getLastName());
                    break;
                case MESSAGE:
                    Message message = messageService.findMessageById(entityId);
                    if (message != null) {
                        Person author = personService.findPersonById(message.getAuthorId());
                        notificationDto.setMainEntity(author.getId() != person.getId() ? author : null);
                        notificationDto.setInfo(message.getMessageText());

                    } else return null;
                    break;
            }

            notificationDto.setId(notification.getId());
            notificationDto.setNotificationType(notificationTypeCode);
            notificationDto.setSentTime(notification.getSentTime());

            return notificationDto;
        }).filter(notificationDto -> notificationDto != null && notificationDto.getMainEntity() != null)
                .collect(Collectors.toList());
        return new ListResponseDto<>((long) notificationDtoList.size(), offset, itemsPerPage, notificationDtoList);
    }

    private MainEntity getEntityById(Integer entityId, NotificationTypeEnum notificationTypeCode) {
        MainEntity entity = null;
        switch (notificationTypeCode) {
            case POST:
                PostDto postDto = postService.getPostDtoById(entityId, null);

                break;
            case POST_COMMENTS:
            case COMMENT_COMMENT:
                break;
            case FRIEND_BIRTHDAY:
                break;
            case FRIEND_REQUEST:
                entity = personService.findPersonById(entityId);
                break;
            case MESSAGE:
                break;
        }
        return entity;
    }
}
