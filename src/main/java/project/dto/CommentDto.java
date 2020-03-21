package project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CommentDto {
    private CommentModelDto commentModel;

    private Integer id;

    @JsonProperty("post_id")
    private Integer postId;

    private Date time;

    @JsonProperty("author_id")
    private Integer authorId;

    @JsonProperty("is_blocked")
    private boolean isBlocked;

}
