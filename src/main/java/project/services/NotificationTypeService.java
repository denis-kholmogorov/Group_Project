package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.models.NotificationType;
import project.models.enums.NotificationTypeEnum;
import project.repositories.NotificationRepository;
import project.repositories.NotificationTypeRepository;

@Service
public class NotificationTypeService {

    private NotificationTypeRepository notificationTypeRepository;

    @Autowired
    public NotificationTypeService(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    public NotificationType findByCode(NotificationTypeEnum code) {
        return notificationTypeRepository.findByCode(code);
    }


}
