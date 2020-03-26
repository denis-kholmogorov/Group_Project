package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import project.models.ResponseModel;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ListResponseDto<T> extends ResponseModel {

    private Long total;

    private Integer offset;

    private Integer perPage;

    private List<T> data;
}