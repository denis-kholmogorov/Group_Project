package project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.models.NotificationType;
import project.models.enums.NotificationTypeEnum;

@Repository
public interface NotificationTypeRepository extends CrudRepository<NotificationType, Integer> {
    NotificationType findByCode(NotificationTypeEnum code);


}
