package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class AbstractResponse {

    private String error = "";

    private Long timestamp = new Date().getTime();

}

