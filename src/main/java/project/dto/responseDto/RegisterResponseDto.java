package project.dto.responseDto;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class RegisterResponseDto extends ResponseDto
{
    private String error = "";

    private Long timestamp = new Date().getTime();

    private Map<String,String> data = new HashMap<>();
}
