package project.dto.responseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseDataObject<T> extends AbstractResponse {

    private T data;
}

