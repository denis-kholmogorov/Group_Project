package project.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import project.models.MainEntity;
import project.models.NotificationType;
import project.models.Person;

import javax.persistence.JoinColumn;
import java.util.Date;

@Data
public class NotificationDto {

    private Integer id;

    @JsonProperty("type_id")
    private Integer notificationType;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonProperty("sent_time")
    private Date sentTime;

    @JsonProperty("entity_author")
    private Integer entity;

    private String info;
}
