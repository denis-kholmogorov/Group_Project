package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequestDto {

    String email;

    String password;

    String firstName;

    String lastName;

}
