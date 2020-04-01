package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.dto.responseDto.NotificationSettingsResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.NotificationType;
import project.models.Person;
import project.models.PersonNotificationSetting;
import project.repositories.PersonNotificationSettingsRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PersonNotificationSettingsService {

    PersonNotificationSettingsRepository personNotificationSettingsRepository;

    public ResponseDto<List<NotificationSettingsResponseDto>> findAllByPerson(Person person) {
        List<PersonNotificationSetting> settingList = personNotificationSettingsRepository.findAllByPerson(person);
        List<NotificationSettingsResponseDto> dtoSettingList = new ArrayList<>();
        if (settingList.size() != 0) {
            dtoSettingList = settingList.stream().map(
                    setting -> new NotificationSettingsResponseDto(
                            "",
                            setting.getNotificationType().getName(),
                            setting.getNotificationType().getCode(),
                            setting.getEnable()))
                    .collect(toList());
        }
        return new ResponseDto<>(dtoSettingList);
    }

    public ResponseDto<PersonNotificationSetting> updateNotificationSetting(
            Person person, NotificationType notificationType, Boolean enable) {
        PersonNotificationSetting setting = personNotificationSettingsRepository
                .findByNotificationTypeAndPerson(notificationType, person).orElse(null);
        if (setting != null) setting.setEnable(enable);
        else setting = new PersonNotificationSetting(person, notificationType, enable);
        return new ResponseDto<>(personNotificationSettingsRepository.save(setting));
    }
}
