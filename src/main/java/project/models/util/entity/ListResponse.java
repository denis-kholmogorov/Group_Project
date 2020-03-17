package project.models.util.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListResponse extends ResponseDataArray {

    private int total;

    private int offset;

    private int perPage = 20;
}
