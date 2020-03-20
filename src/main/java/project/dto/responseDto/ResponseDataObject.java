package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResponseDataObject<T> extends ResponseDto {

    private T data;
}

