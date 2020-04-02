package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.models.NotificationType;
import project.models.enums.NotificationTypeEnum;
import project.repositories.NotificationTypeRepository;

@Service
@AllArgsConstructor
public class NotificationTypeService {

    private NotificationTypeRepository notificationTypeRepository;


    public NotificationType findByCode(NotificationTypeEnum code) {
        return notificationTypeRepository.findByCode(code);
    }
}
