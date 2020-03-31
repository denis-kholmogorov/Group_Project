package project.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.dto.responseDto.NotificationSettingsResponseDto;
import project.dto.responseDto.ResponseDto;
import project.models.NotificationType;
import project.models.PersonNotificationSetting;
import project.repositories.PersonNotificationSettingsRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PersonNotificationSettingsService {

    PersonNotificationSettingsRepository personNotificationSettingsRepository;

    public ResponseDto<List<NotificationSettingsResponseDto>> findAllByPersonId(Integer personId) {
        List<PersonNotificationSetting> settingList = personNotificationSettingsRepository.findAllByPersonId(personId);
        List<NotificationSettingsResponseDto> dtoSettingList = settingList.stream().map(
                setting -> new NotificationSettingsResponseDto(
                        "",
                        setting.getNotificationType().getName(),
                        setting.getNotificationType().getCode(),
                        setting.getEnable()))
                .collect(toList());
        return new ResponseDto<>(dtoSettingList);
    }

    public ResponseDto<PersonNotificationSetting> updateNotificationSetting(
            Integer personId, NotificationType notificationType, Boolean enable) {
        PersonNotificationSetting setting = personNotificationSettingsRepository
                .findByNotificationTypeAndPersonId(notificationType, personId).orElse(null);
        if (setting != null) setting.setEnable(enable);
        else setting = new PersonNotificationSetting(personId, notificationType, enable);
        return new ResponseDto<>(personNotificationSettingsRepository.save(setting));
    }
}
