package project.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewWallPostDto {

    private String title;

    @JsonProperty("post_text")
    private String postText;
}
