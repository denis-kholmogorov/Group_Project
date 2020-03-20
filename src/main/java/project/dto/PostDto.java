package project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.Person;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostDto {
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime time;

    private Person author;

    private String title;

    private String postText;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    private Integer likes;

    private List<CommentDto> comments;
}
