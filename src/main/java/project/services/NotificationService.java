package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dto.responseDto.ListResponseDto;
import project.dto.responseDto.NotificationDto;
import project.repositories.NotificationRepository;


@Service
public class NotificationService {

    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public ListResponseDto<NotificationDto> findAllNotificationsByPersonId(
            Integer personId, Integer offset, Integer itemsPerPage) {
        return null;
    }
}
