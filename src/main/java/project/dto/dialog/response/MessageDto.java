package project.dto.dialog.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.Dialog;
import project.models.enums.ReadStatus;

import javax.persistence.*;
import java.util.Calendar;

@Data
@NoArgsConstructor
public class MessageDto
{
    private Integer id;

    private Long time;

    @JsonProperty(value = "author_id")
    private Integer authorId;

    @JsonProperty(value = "recipient_id")
    private Integer recipientId;

    @JsonProperty(value = "message_text")
    private String messageText;

    @JsonProperty(value = "read_status")
    private ReadStatus readStatus;
}
