package project.controllers.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.dto.responseDto.ListResponseDto;
import project.models.Notification;
import project.models.Person;
import project.security.TokenProvider;
import project.services.PersonNotificationSettingsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private PersonNotificationSettingsService personNotificationSettingsService;

    @GetMapping
    public ResponseEntity getNotifications(@RequestParam(required = false, name = "name") String name, @RequestParam(defaultValue = "0") Integer offset,
                                           @RequestParam(defaultValue = "20") Integer itemPerPage, HttpServletRequest request){

        log.info("trig1");
        Person person = tokenProvider.getPersonByRequest(request);
        List<Notification> notifications = person.getNotifications();
        return ResponseEntity.ok(new ListResponseDto<>((long) notifications.size(), offset, itemPerPage, notifications));
    }
}
