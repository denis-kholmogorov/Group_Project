package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public
class LoginResponseDto
{
    String email;

    String token;
}
