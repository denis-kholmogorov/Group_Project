package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class RegisterResponseDto extends AbstractResponse
{
    private String error = "";

    private Long timestamp = new Date().getTime();

    private Map<String,String> data = new HashMap<>();
}
