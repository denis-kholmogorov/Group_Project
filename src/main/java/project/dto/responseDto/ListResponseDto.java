package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import project.dto.PersonsWallPostDto;
import project.models.ResponseModel;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ListResponseDto extends ResponseModel {

    private Integer total;

    private Integer offset;

    private Integer perPage;

    private List<PersonsWallPostDto> data;
}
