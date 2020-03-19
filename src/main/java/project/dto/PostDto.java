package project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostDto {
    private Integer id;

    private LocalDateTime time;

    private PersonDto author;

    private String title;

    private String postText;

    @JsonProperty("is_blocked")
    private boolean isBlocked;

    private Integer likes;

    private List<CommentDto> comments;
}
