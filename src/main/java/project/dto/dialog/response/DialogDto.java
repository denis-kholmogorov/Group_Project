package project.dto.dialog.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.models.Message;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DialogDto {

    Integer id;

    @JsonProperty(value = "unread_count")
    Integer unreadCount;

    @JsonProperty(value = "last_message")
    Message message;

    public DialogDto(Message message) {
        this.message = message;
    }
}
