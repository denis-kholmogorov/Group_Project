package project.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.MainEntity;
import project.models.NotificationType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private Integer id;

    @JsonProperty("event_type")
    private NotificationType notificationType;

    @JsonProperty("sent_time")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date sentTime;

    @JsonProperty("entity_author")
    private MainEntity mainEntity;

    private String info;
}
