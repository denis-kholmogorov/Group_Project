package project.dto.responseDto;
import lombok.Data;

import java.util.Date;

@Data
public class ResponseDto {

    private String error = "";

    private Long timestamp = new Date().getTime();

}

