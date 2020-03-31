package project.repositories;

import org.springframework.data.repository.CrudRepository;
import project.models.NotificationType;
import project.models.PersonNotificationSetting;

import java.util.List;
import java.util.Optional;

public interface PersonNotificationSettingsRepository extends CrudRepository<PersonNotificationSetting, Integer> {

    List<PersonNotificationSetting> findAllByPersonId(Integer personId);

    Optional<PersonNotificationSetting> findByNotificationTypeAndPersonId(NotificationType notificationType, Integer personId);
}

