package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.repositories.NotificationRepository;

@Service
public class NotificationTypeService {

    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationTypeService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


}
