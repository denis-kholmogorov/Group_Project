package project.dto.dialog.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.dto.responseDto.ResponseDto;
import project.models.ResponseModel;

import java.util.Date;

@Data
@AllArgsConstructor
public class DialogsResponseDto<T> extends ResponseModel
{
    private Integer offset = 0;

    private Integer total = 20;

    private Integer perPage = 10;

    private Long last_online_time = new Date().getTime();

    private T data;

    public DialogsResponseDto(T data) {
        this.data = data;
    }

}
