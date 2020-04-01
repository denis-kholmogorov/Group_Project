package project.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.MainEntity;
import project.models.NotificationType;
import project.models.Person;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private Integer id;

    @JsonProperty("type_id")
    private NotificationType notificationType;

    @JsonProperty("sent_time")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date sentTime;

    @JsonProperty("peron_id")
    private Person person;

    @JsonProperty("entity_id")
    private MainEntity mainEntity;

    private String info;
}
