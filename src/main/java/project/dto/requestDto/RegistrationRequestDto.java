package project.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequestDto {

    String email;

    String passwd1;

    String passwd2;

    String firstName;

    String lastName;

    String code;


}
