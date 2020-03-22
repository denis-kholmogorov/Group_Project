package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.models.ResponseModel;

@Data
@AllArgsConstructor
public class ResponseDto<T> {

    private ResponseModel responseModel;

    private T data;

    public ResponseDto(T data) {
        this.data = data;
    }
}
