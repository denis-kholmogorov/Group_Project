package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ResponseDto<T> {

    private String error = "";

    private Long timestamp = new Date().getTime();

    private T data;

    public ResponseDto(T data) {
        this.data = data;
    }
}
