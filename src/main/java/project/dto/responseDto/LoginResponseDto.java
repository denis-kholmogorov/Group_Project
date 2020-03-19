package project.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto extends AbstractResponse
{

    String email;

    String token;
}
