package project.repositories;

import org.springframework.data.repository.CrudRepository;
import project.models.NotificationType;
import project.models.enums.NotificationTypeEnum;

public interface NotificationTypeRepository extends CrudRepository<NotificationType, Integer> {
    NotificationType findByCode(NotificationTypeEnum code);
}
