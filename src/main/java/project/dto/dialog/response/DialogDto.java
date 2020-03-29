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

    Integer id = 1;

    @JsonProperty(value = "unread_count")
    Integer unreadCount = 12;

    @JsonProperty(value = "last_message")
    MessageDto message;

    public DialogDto(MessageDto message) {
        this.message = message;
    }
}
