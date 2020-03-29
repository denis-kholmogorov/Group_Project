package project.dto.dialog.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.Person;
import project.models.enums.ReadStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
@NoArgsConstructor
public class MessageDto
{
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date time;

    @JsonProperty(value = "author_id")
    private Integer authorId;

    @JsonProperty(value = "recipient")
    private Person recipient;

    @JsonProperty(value = "message_text")
    private String messageText;

    @JsonProperty(value = "read_status")
    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;
}
