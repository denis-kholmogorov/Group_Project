package project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.dto.responseDto.NotificationSettingsResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.NotificationType;
import project.models.Person;
import project.models.PersonNotificationSetting;
import project.repositories.PersonNotificationSettingsRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class PersonNotificationSettingsService {

    PersonNotificationSettingsRepository personNotificationSettingsRepository;

    public ResponseDto<List<NotificationSettingsResponseDto>> findAllByPerson(Person person) {
        List<PersonNotificationSetting> settingList = person.getNotificationSettings();
        List<NotificationSettingsResponseDto> dtoSettingList = settingList.stream().map(
                    setting -> new NotificationSettingsResponseDto(
                            "",
                            setting.getNotificationType().getName(),
                            setting.getNotificationType().getCode(),
                            setting.getEnable()))
                    .collect(toList());
        dtoSettingList.forEach(setting -> {
            log.info(setting.toString());
        });
        return new ResponseDto<>(dtoSettingList);
    }

    public ResponseDto<PersonNotificationSetting> updateNotificationSetting(
            Person person, NotificationType notificationType) {
        PersonNotificationSetting setting = personNotificationSettingsRepository
                .findByNotificationTypeAndPerson(notificationType, person).orElse(null);
        if (setting != null){
            if (setting.getEnable()) {
                setting.setEnable(false);
            } else {
                setting.setEnable(true);
            }
        }
        return new ResponseDto<>(personNotificationSettingsRepository.save(setting));
    }
}
