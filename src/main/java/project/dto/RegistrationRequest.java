package project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RegistrationRequest {

    public RegistrationRequest(String email, String password, String firstName, String lastName, String code) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
    }

    public RegistrationRequest() {
    }

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String code;

}
