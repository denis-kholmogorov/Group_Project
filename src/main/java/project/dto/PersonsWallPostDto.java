package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonsWallPostDto {
    private PostDto postDto;
    private String type;
}
