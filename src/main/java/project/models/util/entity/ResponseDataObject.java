package project.models.util.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseDataObject<T> extends Response {

    private T data;
}
