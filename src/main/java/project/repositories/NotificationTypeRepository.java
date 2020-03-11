package project.repositories;

import org.springframework.data.repository.CrudRepository;
import project.models.NotificationType;

public interface NotificationTypeRepository extends CrudRepository<NotificationType, Integer> {
}
