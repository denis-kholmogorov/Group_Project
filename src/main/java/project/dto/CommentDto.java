package project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private CommentModelDto commentModel;

    private Integer id;

    @JsonProperty("post_id")
    private Integer postId;

    private LocalDateTime time;

    @JsonProperty("author_id")
    private Integer authorId;

    @JsonProperty("is_blocked")
    private boolean isBlocked;

}
