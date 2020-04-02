package project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.NotificationDto;
import project.models.Notification;
import project.models.Person;
import project.repositories.NotificationRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class NotificationService {

    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public ListResponseDto<NotificationDto> findAllNotificationsByPersonId(
            Person person, Integer offset, Integer itemsPerPage) {
        log.info("trig notif");
        List<Notification> notificationList = person.getNotificationList().subList(
                offset, Math.min(itemsPerPage, person.getNotificationList().size()));
        List<NotificationDto> notificationDtoList = notificationList.stream().map(notification -> {
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setId(notification.getId());
            notificationDto.setNotificationType(notification.getNotificationType().getId());
            notificationDto.setSentTime(new Date());
            notificationDto.setMainEntity(notification.getMainEntity().getId());
            notificationDto.setInfo("info");
            return notificationDto;
        }).collect(Collectors.toList());
        return new ListResponseDto<>((long) notificationDtoList.size(), offset, itemsPerPage, notificationDtoList);
    }
}
