package project.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.NotificationDto;
import project.models.MainEntity;
import project.models.Notification;
import project.models.NotificationType;
import project.models.Person;
import project.models.enums.NotificationTypeEnum;
import project.repositories.NotificationRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
            //Integer entityId = notification.getMainEntity().getId();
            NotificationTypeEnum notificationTypeCode = notification.getNotificationType().getCode();
            notificationDto.setId(notification.getId());
            notificationDto.setNotificationType(notificationTypeCode);
            notificationDto.setSentTime(new Date());
            //notificationDto.setEntity(getEntityById(entityId, notificationTypeCode));
            notificationDto.setEntity(notification.getMainEntity());
            notificationDto.setInfo("info");
            return notificationDto;
        }).collect(Collectors.toList());
        return new ListResponseDto<>((long) notificationDtoList.size(), offset, itemsPerPage, notificationDtoList);
    }

    private MainEntity getEntityById(Integer entityId, NotificationTypeEnum notificationTypeCode) {
        MainEntity entity = null;
        switch (notificationTypeCode) {
            case POST:
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
