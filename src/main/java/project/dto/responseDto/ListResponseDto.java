package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.ResponseModel;

import java.util.List;

@Data
@AllArgsConstructor
public class ListResponseDto<T> extends ResponseModel {

    private Integer total;

    private Integer offset;

    private Integer perPage;

    private List<T> data;
}
