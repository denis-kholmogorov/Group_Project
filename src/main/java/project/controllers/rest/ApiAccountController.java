package project.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dto.requestDto.NotificationSettingDto;
import project.dto.requestDto.PasswordSetDto;
import project.dto.requestDto.RegistrationRequestDto;
import project.dto.responseDto.MessageResponseDto;
import project.dto.responseDto.ResponseDto;
import project.handlerExceptions.BadRequestException400;
import project.models.NotificationType;
import project.models.PersonNotificationSetting;
import project.security.TokenProvider;
import project.services.NotificationService;
import project.services.NotificationTypeService;
import project.services.PersonNotificationSettingsService;
import project.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/account/")
@AllArgsConstructor
public class ApiAccountController {
    private PersonService personService;
    private NotificationService notificationService;
    private PersonNotificationSettingsService personNotificationSettingsService;
    private TokenProvider tokenProvider;
    private NotificationTypeService notificationTypeService;

    @PostMapping(value = "register")
    public ResponseEntity<ResponseDto<MessageResponseDto>> register(@RequestBody RegistrationRequestDto dto) throws BadRequestException400 {
        log.info("контроллер Register отработал");
        personService.registrationPerson(dto);
        return ResponseEntity.ok(new ResponseDto(new MessageResponseDto()));
    }

    @PutMapping(value = "password/recovery")
    public ResponseEntity<ResponseDto<MessageResponseDto>> sendRecoveryEmail(@RequestBody Map<String, String> email) throws BadRequestException400 {
        return ResponseEntity.ok(personService.sendRecoveryPasswordEmail(email.get("email")));
    }

    @PutMapping("password/set")
    public ResponseEntity<ResponseDto<MessageResponseDto>> setNewPassword(@RequestBody PasswordSetDto passwordSetDto, HttpServletRequest request) throws BadRequestException400 {
        return ResponseEntity.ok(personService.setNewPassword(passwordSetDto, request));
    }

    @GetMapping("notifications")
    public ResponseEntity<?> getNotificationSettings(HttpServletRequest servletRequest) {
        Integer personId = tokenProvider.getPersonByRequest(servletRequest).getId();
        return ResponseEntity.ok(personNotificationSettingsService.findAllByPersonId(personId));
    }

    @PutMapping("notifications")
    public ResponseEntity<ResponseDto<PersonNotificationSetting>> updatePersonNotificationSettings(
            @RequestBody NotificationSettingDto dto, HttpServletRequest servletRequest) {
        Integer personId = tokenProvider.getPersonByRequest(servletRequest).getId();
        NotificationType notificationType = notificationTypeService.findByCode(dto.getNotificationType());
        return ResponseEntity.ok(personNotificationSettingsService.
                updateNotificationSetting(personId, notificationType, dto.getEnable()));
    }
}

