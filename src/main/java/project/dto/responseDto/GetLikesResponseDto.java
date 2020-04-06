package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetLikesResponseDto {

    private Integer likes;

    private List<Integer> users;

}
