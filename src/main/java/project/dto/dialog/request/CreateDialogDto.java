package project.dto.dialog.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDialogDto {

    @JsonProperty("user_ids")
    private Set<Integer> userIds;

}
