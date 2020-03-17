package project.models.util.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseDataArray extends Response {

    private final List<? super Object> data;

    public ResponseDataArray() {
        this.data = new ArrayList<>();
    }

    public <T> boolean add(T data) {
        return this.data.add(data);
    }
}
