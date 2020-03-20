package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto<T>  {
    private String error;
    private String timestamp;
    private T data;

    public ResponseDto(T data) {
        this.data = data;
    }
}
